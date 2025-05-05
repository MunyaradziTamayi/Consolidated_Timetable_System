package com.app.timetable.Entity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaffTimetableEntity {

    private String date;
    private String time;
    private String venue;
    private String staff;

    public StaffTimetableEntity() {
        // Default constructor
    }

    public StaffTimetableEntity(String date, String time, String venue, String staff) {

        this.date = date;
        this.time = time;
        this.venue = venue;
        this.staff = staff;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }


}

