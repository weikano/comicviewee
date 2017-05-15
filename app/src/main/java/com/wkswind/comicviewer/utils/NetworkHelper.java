package com.wkswind.comicviewer.utils;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.net.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by �Ϸ粻�� on 2017/5/14.
 */
public class NetworkHelper {
    private static final long CONNECTION_TIMEOUT = 60;
    private static final long READ_TIMEOUT = 60;
    private static final long WRITE_TIMEOUT = 60;
//    private static Retrofit instance;
    private static final long CACHE_SIZE = 10 * 1024 * 1024;
    private static OkHttpClient client;
    private static Map<HttpUrl, Retrofit> maps = Collections.synchronizedMap(new HashMap<HttpUrl, Retrofit>());

    public static OkHttpClient getClientInstance(Context context) {
        if(client == null) {
            synchronized (NetworkHelper.class) {
                if(client == null) {
                    client = new OkHttpClient.Builder().cache(new Cache(cache(context), CACHE_SIZE)).proxy(proxy()).addInterceptor(logInterceptor()).connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).build();
                }
            }
        }
        return client;
    }
    public static Retrofit getInstance(Context context, HttpUrl baseUrl) {
        context = context.getApplicationContext();
        Retrofit instance = maps.get(baseUrl);
        if (instance == null) {
            synchronized (NetworkHelper.class) {
                if (instance == null) {
                    instance = new Retrofit.Builder().client(getClientInstance(context)).baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
                    maps.put(baseUrl, instance);
                }
            }
        }
        return instance;
    }

    private static Interceptor logInterceptor() {
        return LogInterceptor.getInstance();
    }

    private static Proxy proxy() {
        return null;
    }

    private static File cache(Context context) {
        return context.getExternalCacheDir();
    }
}
