package com.wkswind.comicviewer.utils;

import android.content.Context;

import com.wkswind.comicviewer.bean.DaoMaster;
import com.wkswind.comicviewer.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2017/5/16.
 */

public final class DatabaseHelper {
    private static volatile DaoSession session;
    public static DaoSession getInstance(Context context) {
        if(session == null) {
            synchronized (DatabaseHelper.class) {
                if(session == null) {
                    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getPackageName());
                    Database db = helper.getWritableDb();
                    session = new DaoMaster(db).newSession();
                }
            }
        }
        return session;
    }
}
