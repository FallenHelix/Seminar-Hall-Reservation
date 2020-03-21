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
    private String floor;
    private String building;

    public Hall() {
    }


    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Hall(String name, int capacity, String dept, String Floor, String building) {
        this.Name = name;
        this.Size = capacity;
        this.dept = dept;
        this.floor=Floor;
        this.building=building;
    }

    protected Hall(Parcel in) {
        Name = in.readString();
        Size = in.readInt();
        key = in.readString();
        dept = in.readString();
        floor=in.readString();
        building=in.readString();
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
        dest.writeString(floor);
        dest.writeString(building);
    }

    public String getDept(){ return dept;}
}


