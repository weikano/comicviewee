package com.wkswind.comicviewer.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.wkswind.comicviewer.BuildConfig;
import com.wkswind.comicviewer.utils.NetworkHelper;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ViewerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, OkHttpImagePipelineConfigFactory.newBuilder(this, NetworkHelper.getClientInstance(this)).build());
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
