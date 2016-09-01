package com.example.yxb.secretary.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.bean
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/29
 * MODIFY_BY:
 */
public class ChatItem implements Parcelable{
    private int type;
    private String text;
    private Bitmap bitmap;

    public ChatItem(){

    }

    protected ChatItem(Parcel in) {
        type = in.readInt();
        text = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ChatItem> CREATOR = new Creator<ChatItem>() {
        @Override
        public ChatItem createFromParcel(Parcel in) {
            return new ChatItem(in);
        }

        @Override
        public ChatItem[] newArray(int size) {
            return new ChatItem[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(text);
        dest.writeParcelable(bitmap, flags);
    }
}
