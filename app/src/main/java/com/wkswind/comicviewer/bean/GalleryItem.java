package com.wkswind.comicviewer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/15.
 */

public class GalleryItem implements Parcelable {
    private String index;
    private String background;
    private String title;
    private String info;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.index);
        dest.writeString(this.background);
        dest.writeString(this.title);
        dest.writeString(this.info);
    }

    public GalleryItem() {
    }

    protected GalleryItem(Parcel in) {
        this.index = in.readString();
        this.background = in.readString();
        this.title = in.readString();
        this.info = in.readString();
    }

    public static final Parcelable.Creator<GalleryItem> CREATOR = new Parcelable.Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel source) {
            return new GalleryItem(source);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };
}
