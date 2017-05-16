package com.wkswind.comicviewer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;

/**
 * Created by Administrator on 2017/5/15.
 */
@Entity
public class GalleryItem implements Parcelable {
    @Id
    private String index;
    private String background;
    private String title;
    private String info;
    private String category;
    private boolean star;

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        dest.writeString(this.category);
        dest.writeInt(this.star ? 1 : 0);
    }

    public boolean getStar() {
        return this.star;
    }

    public GalleryItem() {
    }

    protected GalleryItem(Parcel in) {
        this.index = in.readString();
        this.background = in.readString();
        this.title = in.readString();
        this.info = in.readString();
        this.category = in.readString();
        this.star = in.readInt() == 1;
    }

    @Generated(hash = 1994678801)
    public GalleryItem(String index, String background, String title, String info, String category,
            boolean star) {
        this.index = index;
        this.background = background;
        this.title = title;
        this.info = info;
        this.category = category;
        this.star = star;
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
