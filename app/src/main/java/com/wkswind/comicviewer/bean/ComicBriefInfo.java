package com.wkswind.comicviewer.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by �Ϸ粻�� on 2017/5/14.
 */
@Entity
public class ComicBriefInfo {
    @Id(autoincrement = true)
    private Long id;
    private String bookname;
    private String author;
    private String date;
    private String tags;
    private String pageDescription;
    private String cover;

    @Generated(hash = 1325070195)
    public ComicBriefInfo(Long id, String bookname, String author, String date,
            String tags, String pageDescription, String cover) {
        this.id = id;
        this.bookname = bookname;
        this.author = author;
        this.date = date;
        this.tags = tags;
        this.pageDescription = pageDescription;
        this.cover = cover;
    }

    @Generated(hash = 1508298913)
    public ComicBriefInfo() {
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "ComicBriefInfo{" +
                "bookname='" + bookname + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", tags='" + tags + '\'' +
                ", pageDescription='" + pageDescription + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
