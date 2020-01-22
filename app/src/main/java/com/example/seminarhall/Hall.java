package com.example.seminarhall;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties

public class Hall implements Parcelable {
    private String Name;
    private int Size;
    private String key;

    public Hall() {
    }



    public Hall(String name, int capacity, String key) {
        this.Name = name;
        this.key=key;
        this.Size = capacity;
    }

    protected Hall(Parcel in) {
        Name = in.readString();
        Size = in.readInt();
        key = in.readString();
    }

    public static final Creator<Hall> CREATOR = new Creator<Hall>() {
        @Override
        public Hall createFromParcel(Parcel in) {
            return new Hall(in);
        }

        @Override
        public Hall[] newArray(int size) {
            return new Hall[size];
        }
    };

    public void setName(String name) {
        this.Name = name;
    }

    public void setSize(int size) {
        this.Size = size;
    }

    public void setKey(String key) {
        this.key=key;
    }

    public String getKey()
    {
        return this.key;
    }

    public String getName() {
        return Name;
    }

    public int getSize() {
        return Size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeInt(Size);
        dest.writeString(key);
    }
}


