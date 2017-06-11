package com.wkswind.comicviewer.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.wkswind.comicviewer.R;

import java.io.File;

/**
 * Created by 南风不竞 on 2017/6/10.
 */

public final class NotificationHelper {
    public static void showFinish(Context context, File dest) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setContentTitle(dest.getName());
        builder.setContentText(dest.getName());
        builder.setSubText(dest.getName());
        builder.setSmallIcon(R.drawable.ic_menu_send);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, builder.build());
    }
}
