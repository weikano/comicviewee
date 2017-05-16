package com.wkswind.comicviewer;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.wkswind.comicviewer.base.BaseActivity;
import com.wkswind.comicviewer.bean.ComicDetail;
import com.wkswind.comicviewer.bean.GalleryItem;
import com.wkswind.comicviewer.utils.ContentParser;
import com.wkswind.comicviewer.utils.ParamHelper;
import com.wkswind.comicviewer.utils.UIEvent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private Button enter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        enter = (Button) findViewById(R.id.button);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComicDetail detail = (ComicDetail) v.getTag();
                if(detail != null) {
                    ParamHelper.viewComic(DetailActivity.this, detail);
                }
            }
        });
        final GalleryItem item = ParamHelper.get(getIntent().getExtras(), GalleryItem.class);
        getSupportActionBar().setTitle(item.getTitle());
        getSupportActionBar().setSubtitle(item.getInfo());
        addDisposable(api.detail(item.getIndex()).subscribeOn(Schedulers.io()).map(new Function<Response<ResponseBody>, UIEvent>() {
            @Override
            public UIEvent apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                Document document = Jsoup.parse(responseBodyResponse.body().string());
                return UIEvent.ofResponse(ContentParser.parseDetail(document));
            }
        }).startWith(UIEvent.ofLoading()).onErrorReturn(new Function<Throwable, UIEvent>() {
            @Override
            public UIEvent apply(Throwable throwable) throws Exception {
                return UIEvent.ofError(throwable);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<UIEvent>() {
            @Override
            public void onNext(UIEvent value) {
                enter.setEnabled(!value.loading());
                if(value.complete()) {
                    enter.setTag(value.getResponse());
                    Timber.i("Response " + value.getResponse());
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
}
