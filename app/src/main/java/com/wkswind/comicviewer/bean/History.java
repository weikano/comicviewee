package com.wkswind.comicviewer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Administrator on 2017/5/16.
 */
@Entity
public class History implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String itemId;
    @ToOne(joinProperty = "itemId")
    private GalleryItem item;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.item, flags);
        dest.writeString(this.itemId);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2108826037)
    public GalleryItem getItem() {
        String __key = this.itemId;
        if (item__resolvedKey == null || item__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GalleryItemDao targetDao = daoSession.getGalleryItemDao();
            GalleryItem itemNew = targetDao.load(__key);
            synchronized (this) {
                item = itemNew;
                item__resolvedKey = __key;
            }
        }
        return item;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 107283075)
    public void setItem(GalleryItem item) {
        synchronized (this) {
            this.item = item;
            itemId = item == null ? null : item.getIndex();
            item__resolvedKey = itemId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 851899508)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHistoryDao() : null;
    }

    public History() {
    }

    protected History(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.item = in.readParcelable(GalleryItem.class.getClassLoader());
        this.itemId = in.readString();
    }

    @Generated(hash = 1848846813)
    public History(Long id, String itemId) {
        this.id = id;
        this.itemId = itemId;
    }

    public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1462128466)
    private transient HistoryDao myDao;
    @Generated(hash = 1457923192)
    private transient String item__resolvedKey;
}
