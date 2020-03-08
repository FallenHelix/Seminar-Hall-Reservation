package com.example.seminarhall;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ReservedHall {
    private String hallId,reservationId, startTime,EndTime,userId,Purpose;
    private String StartDate,EndDate;
    private Date BookingDate;
    int NoOfDays;
    List<String> days;

    public int getNoOfDays() {
        return NoOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        NoOfDays = noOfDays;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

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

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        this.StartDate = startDate;
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

//    @Exclude
    public Date getBookingDate() {
        return BookingDate;
    }

//    @Exclude
    public void setBookingDate(Date bookingDate) {
        BookingDate = bookingDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }


    public ReservedHall(String hallId, List<String> Dates
            , String startTime, String endTime, String userId, String purpose) {
        this.hallId = hallId;
        this.StartDate = Dates.get(0);
        this.EndDate=Dates.get(Dates.size()-1);
        this.EndTime = endTime;
        this.startTime = startTime;
        this.userId = userId;
        this.Purpose = purpose;
        this.BookingDate= Calendar.getInstance().getTime();
        this.days=Dates;
        this.NoOfDays=Dates.size();

    }
}
