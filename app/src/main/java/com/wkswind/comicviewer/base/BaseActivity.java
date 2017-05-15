package com.wkswind.comicviewer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wkswind.comicviewer.api.ViewerWNAcg;
import com.wkswind.comicviewer.utils.NetworkHelper;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BaseActivity extends AppCompatActivity {
    private CompositeDisposable disposable = new CompositeDisposable();

    protected void addDisposable(Disposable d) {
        disposable.add(d);
    }

    protected ViewerWNAcg api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = NetworkHelper.getInstance(this, ViewerWNAcg.URL);
        api = retrofit.create(ViewerWNAcg.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();

    }
}
