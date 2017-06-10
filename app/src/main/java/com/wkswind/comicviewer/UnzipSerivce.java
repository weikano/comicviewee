package com.wkswind.comicviewer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UnzipSerivce extends IntentService {

    public static void unzip(Context context, BaseDownloadTask task) {
        Intent intent = new Intent(context, UnzipSerivce.class);
        intent.putExtra(UnzipSerivce.class.getName(), task.getTargetFilePath());
        context.startService(intent);
    }

    public UnzipSerivce() {
        super("UnzipSerivce");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String source = intent.getStringExtra(getClass().getName());
            File sourceFile = new File(source);
            File dest = new File(sourceFile.getParent(), sourceFile.getName().replace(".zip",""));
            if(!dest.exists()){
                dest.mkdirs();
            }
            try {
                ZipFile zip = new ZipFile(source);
                zip.extractAll(dest.getAbsolutePath());
                sourceFile.delete();
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
    }

}
