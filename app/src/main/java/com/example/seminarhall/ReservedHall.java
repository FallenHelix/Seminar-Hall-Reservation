package com.example.seminarhall;

public class ReservedHall {
    String hallId,reservationId,date,startTime,EndTime,userId,Purpose;

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

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

    public ReservedHall(String hallId, String reservationId, String date, String startTime, String endTime, String userId, String purpose) {
        this.hallId = hallId;
        this.reservationId = reservationId;
        this.date = date;
        this.startTime = startTime;
        EndTime = endTime;
        this.userId = userId;
        Purpose = purpose;
    }
}
