package com.wkswind.comicviewer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wkswind.comicviewer.R;
import com.wkswind.comicviewer.bean.ComicDetail;
import com.wkswind.comicviewer.utils.ContentParser;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ComicDetailAdapter extends RecyclerView.Adapter<ComicDetailViewHolder> {
    private LayoutInflater inflater;
    private ComicDetail detail;
    public ComicDetailAdapter(Context context, ComicDetail detail) {
        inflater = LayoutInflater.from(context);
        this.detail = detail;
    }
    @Override
    public ComicDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComicDetailViewHolder(inflater.inflate(R.layout.item_viewer, parent ,false));
    }

    @Override
    public void onBindViewHolder(ComicDetailViewHolder holder, int position) {
        HttpUrl url = ContentParser.pageUrl(detail, position+1);
        Toast.makeText(inflater.getContext(), url.toString(), Toast.LENGTH_SHORT).show();
        holder.showPicture(url.toString());
    }

    @Override
    public int getItemCount() {
        return detail.getPage();
    }
}
