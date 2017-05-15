package com.wkswind.comicviewer.utils;

import android.support.annotation.NonNull;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import java.io.IOException;

/**
 * Created by �Ϸ粻�� on 2017/5/14.
 */
class LogInterceptor implements Interceptor{
    private LogInterceptor() {

    }
    private static LogInterceptor instance;

    static LogInterceptor getInstance() {
        if(instance == null) {
            synchronized (LogInterceptor.class){
                if(instance == null) {
                    instance = new LogInterceptor();
                }
            }
        }
        return instance;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Timber.i(request.url().toString());
        return chain.proceed(request);
    }
}
