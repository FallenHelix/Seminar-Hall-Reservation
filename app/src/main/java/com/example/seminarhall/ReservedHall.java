package com.example.seminarhall;

import com.google.firebase.firestore.Exclude;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ReservedHall {
    private String hallId,reservationId, StartDate,startTime,EndTime,userId,Purpose,EndDate;
    private String BookingDate;
    int NoOfDays;

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

    List<String> days;
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

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
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
        this.BookingDate= DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());
        this.days=Dates;
        this.NoOfDays=Dates.size();

    }
}
