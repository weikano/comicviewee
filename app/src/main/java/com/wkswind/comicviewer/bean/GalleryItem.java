package com.wkswind.comicviewer.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.wkswind.comicviewer.utils.Type;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

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
    @Convert(columnType = String.class, converter = TypeConverter.class)
    private Type type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
        dest.writeSerializable(this.type);
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
        this.type = (Type) in.readSerializable();
        this.star = in.readInt() == 1;
    }

    @Generated(hash = 1117893131)
    public GalleryItem(String index, String background, String title, String info, Type type, boolean star) {
        this.index = index;
        this.background = background;
        this.title = title;
        this.info = info;
        this.type = type;
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

    public static class TypeConverter implements PropertyConverter<Type, String> {

        @Override
        public Type convertToEntityProperty(String databaseValue) {
            if(TextUtils.isEmpty(databaseValue)) {
                return null;
            }
            for(Type type : Type.values()) {
                if(TextUtils.equals(type.toString(), databaseValue)){
                    return type;
                }
            }
            return Type.HOME;
        }

        @Override
        public String convertToDatabaseValue(Type entityProperty) {
            return entityProperty == null ? Type.HOME.toString() : entityProperty.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GalleryItem that = (GalleryItem) o;

        return index != null ? index.equals(that.index) : that.index == null;

    }

    @Override
    public int hashCode() {
        return index != null ? index.hashCode() : 0;
    }
}
