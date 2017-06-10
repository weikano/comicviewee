package com.wkswind.comicviewer.base;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.wkswind.comicviewer.BuildConfig;
import com.wkswind.comicviewer.UnzipSerivce;
import com.wkswind.comicviewer.utils.NetworkHelper;

import java.net.Proxy;
import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ViewerApplication extends Application implements FileDownloadMonitor.IMonitor {
    private static final long MAX_CACHE_SIZE = 200 * 1024 * 1024; // 100M
    private static final long MAX_CACHE_SIZE_LOW = 50 * 1024 * 1024; // 100M
    private static final long MAX_CACHE_SIZE_LOWEST = 10 * 1024 * 1024; // 100M
    @Override
    public void onCreate() {
        super.onCreate();
        FLog.setMinimumLoggingLevel(1);
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
        FileDownloadLog.NEED_LOG = BuildConfig.DEBUG;
        FileDownloadMonitor.setGlobalMonitor(this);

        FileDownloader.init(this, new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new FileDownloadUrlConnection.Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000)
                .readTimeout(15_000)
                .proxy(Proxy.NO_PROXY))));

    }

    @Override
    public void onRequestStart(int count, boolean serial, FileDownloadListener lis) {

    }

    @Override
    public void onRequestStart(BaseDownloadTask task) {

    }

    @Override
    public void onTaskBegin(BaseDownloadTask task) {

    }

    @Override
    public void onTaskStarted(BaseDownloadTask task) {

    }

    @Override
    public void onTaskOver(BaseDownloadTask task) {
        if(task.getStatus() == FileDownloadStatus.completed) {
            Timber.i("%s has finished", task.getTargetFilePath());
            UnzipSerivce.unzip(this, task);
        }
    }
}
