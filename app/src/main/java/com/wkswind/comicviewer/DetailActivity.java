package com.wkswind.comicviewer;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.liulishuo.filedownloader.FileDownloader;
import com.wkswind.comicviewer.base.BaseActivity;
import com.wkswind.comicviewer.bean.ComicDetail;
import com.wkswind.comicviewer.bean.ComicDetailDao;
import com.wkswind.comicviewer.bean.GalleryItem;
import com.wkswind.comicviewer.utils.ContentParser;
import com.wkswind.comicviewer.utils.CustomTabActivityHelper;
import com.wkswind.comicviewer.utils.CustomTabsHelper;
import com.wkswind.comicviewer.utils.DatabaseHelper;
import com.wkswind.comicviewer.utils.ParamHelper;
import com.wkswind.comicviewer.utils.UIEvent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Administrator on 2017/5/15.
 */

public class DetailActivity extends BaseActivity {
    private SwipeRefreshLayout refreshContainer;
    private Button enter, btnDownload;
    private CustomTabActivityHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new CustomTabActivityHelper();
        final GalleryItem item = ParamHelper.get(getIntent().getExtras(), GalleryItem.class);
        setContentView(R.layout.activity_detail);
        refreshContainer = (SwipeRefreshLayout) findViewById(R.id.refresh_container);
        enter = (Button) findViewById(R.id.button);
        btnDownload = (Button) findViewById(R.id.download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComicDetail detail = (ComicDetail) v.getTag();
                if(detail != null) {
                    FileDownloader.getImpl().create(detail.getDownloadPageUrl()).setPath(ContentParser.downloadPath(DetailActivity.this, detail)).start();
                }
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComicDetail detail = (ComicDetail) v.getTag();
                if(detail != null) {
                    ParamHelper.viewComic(DetailActivity.this, detail, item);
                }
            }
        });


        getSupportActionBar().setTitle(item.getTitle());
        getSupportActionBar().setSubtitle(item.getInfo());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addDisposable(Observable.concat(loadFromDatabase(item), loadFromNetwork(item)).subscribeOn(Schedulers.io()).startWith(UIEvent.ofLoading()).onErrorReturn(new Function<Throwable, UIEvent>() {
            @Override
            public UIEvent apply(Throwable throwable) throws Exception {
                return UIEvent.ofError(throwable);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<UIEvent>() {
            @Override
            public void onNext(UIEvent value) {
                enter.setEnabled(!value.loading());
                btnDownload.setEnabled(!value.loading());
                refreshContainer.setRefreshing(value.loading());
                if(value.complete()) {
                    enter.setTag(value.getResponse());
                    btnDownload.setTag(value.getResponse());
                    Timber.i("Response " + value.getResponse());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        }));

//        CustomTabsClient.bindCustomTabsService(this, CustomTabsHelper.getPackageNameToUse(this), new CustomTabsServiceConnection() {
//            private CustomTabsClient mClient;
//            @Override
//            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
//                mClient = client;
//                mClient.warmup(0);
//                CustomTabsSession session = client.newSession(new CustomTabsCallback());
//                session.mayLaunchUrl(Uri.parse(ContentParser.wrapSlideUrl(item.getIndex())),null,null);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                mClient = null;
//            }
//        });
        helper.bindCustomTabsService(this);
        helper.mayLaunchUrl(Uri.parse(ContentParser.wrapSlideUrl(item.getIndex())),null,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.unbindCustomTabsService(this);
    }

    private Observable<UIEvent> loadFromDatabase(final GalleryItem item) {
        return Observable.create(new ObservableOnSubscribe<UIEvent>() {
            @Override
            public void subscribe(ObservableEmitter<UIEvent> e) throws Exception {
                if(!e.isDisposed()) {
                    ComicDetailDao dao = DatabaseHelper.getInstance(getApplicationContext()).getComicDetailDao();
                    List<ComicDetail> list = dao.queryBuilder().where(ComicDetailDao.Properties.GalleryId.eq(item.getIndex())).build().list();
                    if(list != null && !list.isEmpty()) {
                        e.onNext(UIEvent.ofResponse(list.get(0)));
                    }
                    e.onComplete();
                }
            }
        });
    }

    private Observable<UIEvent> loadFromNetwork(final GalleryItem item) {
        return api.detail(item.getIndex()).map(new Function<Response<ResponseBody>, UIEvent>() {
            @Override
            public UIEvent apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                Document document = Jsoup.parse(responseBodyResponse.body().string());
                return UIEvent.ofResponse(ContentParser.parseDetail(document));
            }
        }).doOnNext(new Consumer<UIEvent>() {
            @Override
            public void accept(UIEvent uiEvent) throws Exception {
                if(uiEvent.complete()) {
                    ComicDetail detail = (ComicDetail) uiEvent.getResponse();
                    if(detail != null) {
                        detail.setGalleryId(item.getIndex());
                    }
                    DatabaseHelper.getInstance(getApplicationContext()).getComicDetailDao().insertOrReplace(detail);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
