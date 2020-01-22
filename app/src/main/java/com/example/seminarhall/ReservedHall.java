package com.example.seminarhall;


import java.sql.Date;
import java.sql.Time;

class T_time
{
   private Time time;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    T_time(Time time) {
        this.time=time;
    }



}
public class ReservedHall {
    int Rkey;
    int H_id;
    T_time startTime,EndTime;
    Date bookDate;

    public ReservedHall(int rkey, int h_id, T_time startTime, T_time endTime, Date bookDate) {
        Rkey = rkey;
        H_id = h_id;
        this.startTime = startTime;
        EndTime = endTime;
        this.bookDate = bookDate;
    }

    public int getRkey() {
        return Rkey;
    }

    public void setRkey(int rkey) {
        Rkey = rkey;
    }

    public int getH_id() {
        return H_id;
    }

    public void setH_id(int h_id) {
        H_id = h_id;
    }

    public T_time getStartTime() {
        return startTime;
    }

    public void setStartTime(T_time startTime) {
        this.startTime = startTime;
    }

    public T_time getEndTime() {
        return EndTime;
    }

    public void setEndTime(T_time endTime) {
        EndTime = endTime;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }
}
