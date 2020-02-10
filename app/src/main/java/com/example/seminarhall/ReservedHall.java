package com.example.seminarhall;

import com.google.firebase.firestore.Exclude;



public class ReservedHall {
    String hallId,reservationId,date,startTime,EndTime,userId,Purpose;

    ReservedHall()
    {
        //no args constructor
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    @Exclude
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public ReservedHall(String hallId, String date, String startTime, String endTime, String userId, String purpose) {
        this.hallId = hallId;
        this.date = date;
        this.startTime = startTime;
        this.EndTime = endTime;
        this.userId = userId;
        this.Purpose = purpose;
    }
}
