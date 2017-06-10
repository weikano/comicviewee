package com.wkswind.comicviewer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;

import com.wkswind.comicviewer.DetailActivity;
import com.wkswind.comicviewer.R;
import com.wkswind.comicviewer.ViewerActivity;
import com.wkswind.comicviewer.bean.ComicDetail;
import com.wkswind.comicviewer.bean.GalleryItem;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ParamHelper {
    public static void put(Intent intent, Parcelable params) {
        String key = params.getClass().getName();
        intent.putExtra(key, params);
    }

    public static <T extends Parcelable> T  get(@NonNull Bundle extra, Class<T> clazz) {
        return extra.getParcelable(clazz.getName());
    }

    public static void openDetail(Context context, GalleryItem item) {
        Intent intent = new Intent(context, DetailActivity.class);
        put(intent, item);
        shot(context, intent);
    }

    public static void viewComic(Context context, ComicDetail item) {
//        Intent intent = new Intent(context, ViewerActivity.class);
//        put(intent, item);
//        shot(context, intent);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(false);
        CustomTabActivityHelper.openCustomTab((Activity) context, builder.build(), Uri.parse(ContentParser.wrapSlideUrl(item.getIndex())), null);
    }

    private static void shot(Context context, Intent intent) {
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
