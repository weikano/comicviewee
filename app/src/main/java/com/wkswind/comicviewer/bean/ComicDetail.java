package com.wkswind.comicviewer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/5/16.
 */
@Entity
public class ComicDetail implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String downloadPageUrl;

    private int page;
    private String category;
    private String tags;
    private String brief;
    private String index;

    private String galleryId;

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadPageUrl() {
        return downloadPageUrl;
    }

    public void setDownloadPageUrl(String downloadPageUrl) {
        this.downloadPageUrl = downloadPageUrl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.downloadPageUrl);
        dest.writeInt(this.page);
        dest.writeString(this.category);
        dest.writeString(this.tags);
        dest.writeString(this.brief);
        dest.writeString(this.index);
        dest.writeString(this.galleryId);
    }

    public ComicDetail() {
    }

    protected ComicDetail(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.downloadPageUrl = in.readString();
        this.page = in.readInt();
        this.category = in.readString();
        this.tags = in.readString();
        this.brief = in.readString();
        this.index = in.readString();
        this.galleryId = in.readString();
    }

    @Generated(hash = 147262810)
    public ComicDetail(Long id, String title, String downloadPageUrl, int page, String category, String tags,
            String brief, String index, String galleryId) {
        this.id = id;
        this.title = title;
        this.downloadPageUrl = downloadPageUrl;
        this.page = page;
        this.category = category;
        this.tags = tags;
        this.brief = brief;
        this.index = index;
        this.galleryId = galleryId;
    }

    public static final Parcelable.Creator<ComicDetail> CREATOR = new Parcelable.Creator<ComicDetail>() {
        @Override
        public ComicDetail createFromParcel(Parcel source) {
            return new ComicDetail(source);
        }

        @Override
        public ComicDetail[] newArray(int size) {
            return new ComicDetail[size];
        }
    };
}
