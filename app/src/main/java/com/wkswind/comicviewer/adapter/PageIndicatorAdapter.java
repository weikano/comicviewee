package com.wkswind.comicviewer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.wkswind.comicviewer.R;

/**
 * Created by Administrator on 2017/5/24.
 */

public class PageIndicatorAdapter extends RecyclerView.Adapter<PageIndicatorAdapter.PageIndicatorViewHolder> {
    OnPageChangeListener onPageChangeListener;
    private final int page;
    private LayoutInflater inflater;
    private int checkedPosition = 0;
    public PageIndicatorAdapter(Context context, int page) {
        inflater = LayoutInflater.from(context);
        this.page = page;
    }
    @Override
    public PageIndicatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageIndicatorViewHolder(inflater.inflate(R.layout.item_page_indicator, parent ,false));
    }

    @Override
    public void onBindViewHolder(PageIndicatorViewHolder holder, int position) {
        holder.bindPage(position, checkedPosition);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public int getItemCount() {
        return page;
    }

    class PageIndicatorViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView label;
        PageIndicatorViewHolder(View itemView) {
            super(itemView);
            label = (CheckedTextView) itemView.findViewById(R.id.page_indicator);
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!label.isChecked()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getLayoutPosition();
                        notifyItemChanged(checkedPosition);
                        if(onPageChangeListener != null) {
                            onPageChangeListener.onPageChange(getAdapterPosition()+1);
                        }
                    }
                }
            });
        }

        void bindPage(int page, int checkedPosition) {
            label.setText(String.valueOf(page+1));
            label.setChecked(getAdapterPosition() == checkedPosition);
        }
    }

    public static interface OnPageChangeListener {
        void onPageChange(int page);
    }
}
