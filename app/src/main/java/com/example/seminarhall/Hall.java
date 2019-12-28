package com.example.seminarhall;

import java.util.Date;

public class Hall {
    private String name;
    private int capacity;
    private Date dt;

    public Hall() {
    }

    public Hall(String name, int capacity, Date dt) {
        this.name = name;
        this.capacity = capacity;
        this.dt = dt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public Date getDt() {
        return dt;
    }
}


