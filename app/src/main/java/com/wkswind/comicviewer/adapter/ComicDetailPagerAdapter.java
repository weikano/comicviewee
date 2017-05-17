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

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/5/17.
 */

public class ComicDetailPagerAdapter extends PagerAdapter {
    private final ComicDetail detail;
    private final ImageDecodeOptions options;
    private LayoutInflater inflater;
    public ComicDetailPagerAdapter(Context context, ComicDetail detail) {
        inflater = LayoutInflater.from(context);
        this.detail = detail;
        options = ImageDecodeOptions.newBuilder().setBitmapConfig(Bitmap.Config.RGB_565).build();
    }
    @Override
    public int getCount() {
        return detail.getPage();
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
        String url = ContentParser.pageUrl(detail, position+1).toString();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setImageDecodeOptions(options)
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
