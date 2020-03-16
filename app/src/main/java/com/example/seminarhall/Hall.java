package com.example.seminarhall;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

import java.util.Date;




@IgnoreExtraProperties
public class Hall implements Parcelable {
    private String Name;
    private int Size;
    private String key;
    private String dept;

    public Hall() {
    }



    public Hall(String name, int capacity, String dept) {
        this.Name = name;
        this.Size = capacity;
        this.dept = dept;
    }

    protected Hall(Parcel in) {
        Name = in.readString();
        Size = in.readInt();
        key = in.readString();
        dept = in.readString();
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

    @Exclude
    public void setKey(String key) {
        this.key=key;
    }

    @Exclude
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
        dest.writeString(dept);
    }

    public String getDept(){ return dept;}
}


