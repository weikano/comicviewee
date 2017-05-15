package com.wkswind.comicviewer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wkswind.comicviewer.R;
import com.wkswind.comicviewer.api.ViewerWNAcg;
import com.wkswind.comicviewer.bean.GalleryItem;

/**
 * Created by Administrator on 2017/5/15.
 */

class GalleryViewHolder extends RecyclerView.ViewHolder {
    private SimpleDraweeView cover;
    private TextView title, info;
    private OnGalleryItemClickListener onGalleryItemClickListener;

    GalleryViewHolder(View itemView) {
        super(itemView);
        cover = (SimpleDraweeView) itemView.findViewById(R.id.cover_photo);
        title = (TextView) itemView.findViewById(R.id.cover_title);
        info = (TextView) itemView.findViewById(R.id.cover_info);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onGalleryItemClickListener != null) {
                    onGalleryItemClickListener.onGalleryItemClick(getAdapterPosition());
                }
            }
        });
    }

    public void setOnGalleryItemClickListener(OnGalleryItemClickListener onGalleryItemClickListener) {
        this.onGalleryItemClickListener = onGalleryItemClickListener;
    }

    void showItem(@NonNull GalleryItem item) {
        cover.setImageURI(ViewerWNAcg.URL.newBuilder().addPathSegment(item.getBackground()).toString());
        title.setText(item.getTitle());
        info.setText(item.getInfo());
    }

    static interface OnGalleryItemClickListener {
        void onGalleryItemClick(int position);
    }
}
