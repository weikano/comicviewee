package com.wkswind.comicviewer.base;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.wkswind.comicviewer.BuildConfig;
import com.wkswind.comicviewer.bean.DaoMaster;
import com.wkswind.comicviewer.utils.NetworkHelper;

import org.greenrobot.greendao.database.Database;

import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ViewerApplication extends Application {
    private static final long MAX_CACHE_SIZE = 200 * 1024 * 1024; // 100M
    private static final long MAX_CACHE_SIZE_LOW = 50 * 1024 * 1024; // 100M
    private static final long MAX_CACHE_SIZE_LOWEST = 10 * 1024 * 1024; // 100M
    @Override
    public void onCreate() {
        super.onCreate();

        DiskCacheConfig disk = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(getExternalCacheDir())
                .setMaxCacheSize(MAX_CACHE_SIZE)
                .setMaxCacheSizeOnLowDiskSpace(MAX_CACHE_SIZE_LOW)
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_CACHE_SIZE_LOWEST)
                .build();

        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, NetworkHelper.getClientInstance(this))
                .setRequestListeners(requestListeners)
                .setMainDiskCacheConfig(disk)
                .build();

        Fresco.initialize(this, config);

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }
}
