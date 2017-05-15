package com.wkswind.comicviewer.api;

import com.wkswind.comicviewer.utils.WNAcgType;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by �Ϸ粻�� on 2017/5/14.
 */
public interface ViewerWNAcg {
    static final HttpUrl URL = new HttpUrl.Builder().scheme("https").host("www.wnacg.org").build();;

    @GET(value = "/albums.html")
    Observable<Response<ResponseBody>> home();

    @GET(value = "{type}")
    Observable<Response<ResponseBody>> category(@Path("type") WNAcgType type);

    @GET(value = "{type}")
    Observable<Response<ResponseBody>> categoryWithPage(@Path("type") String index);

    @GET(value = "{path}")
    Observable<Response<ResponseBody>> detail(@Path("path") String path);
}
