package com.wkswind.comicviewer;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import com.wkswind.comicviewer.adapter.GalleryAdapter;
import com.wkswind.comicviewer.base.BaseActivity;
import com.wkswind.comicviewer.bean.GalleryItem;
import com.wkswind.comicviewer.bean.GalleryItemDao;
import com.wkswind.comicviewer.utils.ContentParser;
import com.wkswind.comicviewer.utils.DatabaseHelper;
import com.wkswind.comicviewer.utils.UIEvent;
import com.wkswind.comicviewer.utils.Type;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView content;
    private GalleryAdapter adapter;
    @IdRes
    private int selectedMenuId = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_container);
        content = (RecyclerView) findViewById(R.id.content);
        content.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        DividerItemDecoration verticalDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        verticalDivider.setDrawable(getResources().getDrawable(R.drawable.item_divider_vertical));
        DividerItemDecoration horizontalDivider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        horizontalDivider.setDrawable(getResources().getDrawable(R.drawable.item_divider_horizontal));
        content.addItemDecoration(horizontalDivider);
        content.addItemDecoration(verticalDivider);

        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        addDisposable(RxNavigationView.itemSelections(navigationView).throttleFirst(200, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).flatMap(new Function<MenuItem, ObservableSource<UIEvent>>() {
            @Override
            public ObservableSource<UIEvent> apply(MenuItem menuItem) throws Exception {
                selectedMenuId = menuItem.getItemId();
                Type type = Type.menuIdToType(menuItem.getItemId());
                return Observable.concat(loadFromDatabase(getApplicationContext(), type), loadFromNetwork(getApplicationContext(), type)).startWith(UIEvent.ofLoading());
            }
        }).filter(new Predicate<UIEvent>() {
            @Override
            public boolean test(UIEvent uiEvent) throws Exception {
                return !uiEvent.complete() || (uiEvent.complete() && !((List<GalleryItem>)uiEvent.getResponse()).isEmpty());
            }
        }).onErrorReturn(new Function<Throwable, UIEvent>() {
            @Override
            public UIEvent apply(Throwable throwable) throws Exception {
                return UIEvent.ofError(throwable);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<UIEvent>() {
            @Override
            public void onNext(UIEvent value) {
                refreshLayout.setRefreshing(value.loading());
                if(value.loading()){
                    drawer.closeDrawer(GravityCompat.START);
                    setTitle(navigationView.getMenu().findItem(selectedMenuId).getTitle());
                }else if(value.error()){
                    Timber.e(value.getException());
                }else if(value.complete()) {
                    showGallery(value.getResponse());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        }));
        if(savedInstanceState != null && savedInstanceState.containsKey(NavigationView.class.getName())) {
            selectedMenuId = savedInstanceState.getInt(NavigationView.class.getName());
        }
        simulateNavigationViewItemClick(navigationView, selectedMenuId);
    }

    private void simulateNavigationViewItemClick(@NonNull NavigationView nav, @IdRes int menuId) {
        MenuItem item = nav.getMenu().findItem(menuId);
        setTitle(item.getTitle());
        nav.setCheckedItem(menuId);
        try {
            Field field = nav.getClass().getDeclaredField("mListener");
            field.setAccessible(true);
            NavigationView.OnNavigationItemSelectedListener listener = (NavigationView.OnNavigationItemSelectedListener) field.get(nav);
            if(listener != null) {
                listener.onNavigationItemSelected(item);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(NavigationView.class.getName(), selectedMenuId);
    }

    private Observable<UIEvent> loadFromDatabase(final Context context, final Type type) {
        return Observable.create(new ObservableOnSubscribe<UIEvent>() {
            @Override
            public void subscribe(ObservableEmitter<UIEvent> e) throws Exception {
                if(!e.isDisposed()) {
                    List<GalleryItem> result = DatabaseHelper.getInstance(context).getGalleryItemDao().queryBuilder().where(GalleryItemDao.Properties.Type.eq(type)).build().list();
                    e.onNext(UIEvent.ofResponse(result));
                    e.onComplete();
                }
            }
        });
    }

    private Observable<UIEvent> loadFromNetwork(final Context context, final Type type) {
        return api.category(type).map(new Function<Response<ResponseBody>, UIEvent>() {
            @Override
            public UIEvent apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                Document document = Jsoup.parse(responseBodyResponse.body().string());
                return UIEvent.ofResponse(ContentParser.parseGalleryItems(document));
            }
        }).doOnNext(new Consumer<UIEvent>() {
            @Override
            public void accept(UIEvent uiEvent) throws Exception {
                if(uiEvent.complete()) {
                    final List<GalleryItem> items = (List<GalleryItem>) uiEvent.getResponse();
                    if(items != null) {
                        for (GalleryItem item : items) {
                            item.setType(type);
                        }
                        DatabaseHelper.getInstance(context).getGalleryItemDao().insertOrReplaceInTx(items);
                    }
                }
            }
        });
    }

    private void showGallery(Object response) {
        final List<GalleryItem> datas = (List<GalleryItem>) response;
        if(adapter == null){
            adapter = new GalleryAdapter(this, datas);
            content.setAdapter(adapter);
        }else{
            final List<GalleryItem> oldDatas = adapter.getDatas();
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldDatas.size();
                }

                @Override
                public int getNewListSize() {
                    return datas.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    GalleryItem oldItem = oldDatas.get(oldItemPosition);
                    GalleryItem newItem = datas.get(newItemPosition);
                    return ObjectHelper.equals(oldItem, newItem);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return areItemsTheSame(oldItemPosition, newItemPosition);
                }
            });
            adapter.setDatas(datas);
            result.dispatchUpdatesTo(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
