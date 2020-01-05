package com.example.seminarhall;

import java.util.Date;

public class Hall {
    private String Name;
    private int Size;
    private String key;

    public Hall() {
    }

    public Hall(String name, int capacity) {
        this.Name = name;
        this.Size = capacity;
    }

    public Hall(String name, int capacity, String key) {
        this.Name = name;
        this.key=key;
        this.Size = capacity;
    }

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

}


