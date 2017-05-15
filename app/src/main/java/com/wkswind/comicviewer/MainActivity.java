package com.wkswind.comicviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import com.wkswind.comicviewer.adapter.GalleryAdapter;
import com.wkswind.comicviewer.api.ViewerWNAcg;
import com.wkswind.comicviewer.base.BaseActivity;
import com.wkswind.comicviewer.bean.GalleryItem;
import com.wkswind.comicviewer.utils.ContentParser;
import com.wkswind.comicviewer.utils.NetworkHelper;
import com.wkswind.comicviewer.utils.UIEvent;
import com.wkswind.comicviewer.utils.WNAcgType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView content;
    private GalleryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_container);
        content = (RecyclerView) findViewById(R.id.content);
        content.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.item_divider));
        content.addItemDecoration(decoration);

        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        addDisposable(RxNavigationView.itemSelections(navigationView).throttleFirst(200, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).flatMap(new Function<MenuItem, ObservableSource<UIEvent>>() {
            @Override
            public ObservableSource<UIEvent> apply(MenuItem menuItem) throws Exception {
                WNAcgType type = WNAcgType.menuIdToType(menuItem.getItemId());
                return api.category(type).map(new Function<Response<ResponseBody>, UIEvent>() {
                    @Override
                    public UIEvent apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Document document = Jsoup.parse(responseBodyResponse.body().string());
                        return UIEvent.ofResponse(ContentParser.parseGalleryItems(document));
                    }
                }).startWith(UIEvent.ofLoading());
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
                }else if(value.error()){
                    Timber.e(value.getException());
                }else if(value.complete()) {
                    Toast.makeText(MainActivity.this, "success \n " + String.valueOf(value.getResponse()), Toast.LENGTH_SHORT).show();
                    showGallery(value.getResponse());
//                    Timber.i("response # %s",String.valueOf(value.getResponse()));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    private void showGallery(Object response) {
        List<GalleryItem> datas = (List<GalleryItem>) response;
        if(adapter == null){
            adapter = new GalleryAdapter(this, datas);
            content.setAdapter(adapter);
        }else{
            adapter.setDatas(datas);
            adapter.notifyDataSetChanged();
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
