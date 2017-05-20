package com.wkswind.comicviewer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wkswind.comicviewer.R;
import com.wkswind.comicviewer.bean.GalleryItem;
import com.wkswind.comicviewer.utils.ParamHelper;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/5/15.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private Context context;
    private List<GalleryItem> datas;
    public GalleryAdapter(Context context, List<GalleryItem> datas) {
        this.context = context;
        this.datas = datas;
    }
    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GalleryViewHolder holder = new GalleryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery_item, parent, false));
        holder.setOnGalleryItemClickListener(new GalleryViewHolder.OnGalleryItemClickListener() {
            @Override
            public void onGalleryItemClick(int position) {
//                ParamHelper.openDetail(context, datas.get(position));
                ParamHelper.openDetail(context, datas.get(position));
            }
        });
        return holder;
    }

    public List<GalleryItem> getDatas() {
        return datas;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        holder.showItem(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setDatas(List<GalleryItem> datas) {
        this.datas = datas;
    }
}
