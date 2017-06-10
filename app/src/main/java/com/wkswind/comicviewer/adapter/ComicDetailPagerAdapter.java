package com.wkswind.comicviewer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wkswind.comicviewer.R;
import com.wkswind.comicviewer.bean.ComicDetail;
import com.wkswind.comicviewer.utils.ContentParser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class ComicDetailPagerAdapter extends PagerAdapter {
    private final ImageDecodeOptions options;
    private final List<File> files;
    private LayoutInflater inflater;
    public ComicDetailPagerAdapter(Context context, ComicDetail detail) {
        inflater = LayoutInflater.from(context);
        options = ImageDecodeOptions.newBuilder().setBitmapConfig(Bitmap.Config.RGB_565).build();
        File dir = ContentParser.downloadDir(context, detail);
        files = Arrays.asList(dir.listFiles());
    }
    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_viewer, container, false);
        SimpleDraweeView image = (SimpleDraweeView) view.findViewById(R.id.viewer_page);
        image.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
//        String url = ContentParser.pageUrl(detail, position+1).toString();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(files.get(position))).setImageDecodeOptions(options)
                .setRotationOptions(RotationOptions.autoRotate()).setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).build();
        image.setController(controller);
//        image.setImageURI(url);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
