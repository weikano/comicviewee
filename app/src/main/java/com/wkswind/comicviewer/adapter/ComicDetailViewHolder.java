package com.wkswind.comicviewer.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wkswind.comicviewer.R;
import com.wkswind.comicviewer.api.ViewerWNAcg;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ComicDetailViewHolder extends RecyclerView.ViewHolder {
    private final ImageDecodeOptions options;
    private SimpleDraweeView image;
    public ComicDetailViewHolder(View itemView) {
        super(itemView);
        image = (SimpleDraweeView) itemView.findViewById(R.id.viewer_page);
        image.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        options = ImageDecodeOptions.newBuilder().setBitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void showPicture(String pageUrl) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(pageUrl)).setImageDecodeOptions(options)
                .setRotationOptions(RotationOptions.autoRotate()).setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).build();
        image.setController(controller);
        image.setImageURI(pageUrl);
    }

}
