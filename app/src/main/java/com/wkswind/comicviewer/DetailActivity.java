package com.wkswind.comicviewer;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.wkswind.comicviewer.base.BaseActivity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GalleryItem item = ParamHelper.get(getIntent().getExtras(), GalleryItem.class);
        addDisposable(api.detail(item.getIndex()).subscribeOn(Schedulers.io()).map(new Function<Response<ResponseBody>, UIEvent>() {
            @Override
            public UIEvent apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                Document document = Jsoup.parse(responseBodyResponse.body().string());
                return UIEvent.ofResponse(document);
            }
        }).startWith(UIEvent.ofLoading()).onErrorReturn(new Function<Throwable, UIEvent>() {
            @Override
            public UIEvent apply(Throwable throwable) throws Exception {
                return UIEvent.ofError(throwable);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<UIEvent>() {
            @Override
            public void onNext(UIEvent value) {
                if(value.complete()) {
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
