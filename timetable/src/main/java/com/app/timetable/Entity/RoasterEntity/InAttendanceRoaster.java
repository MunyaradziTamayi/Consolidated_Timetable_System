package com.app.timetable.Entity.RoasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "in_attendance_roaster")
public class InAttendanceRoaster {

    // Getters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long inAttendanceID;
    private String date;
    private String time;
    private String venue;

    public InAttendanceRoaster() {
        // Default constructor
    }

    public InAttendanceRoaster(Long inAttendanceID,String date,String time,String venue) {

        this.inAttendanceID=inAttendanceID;
        this.date=date;
        this.time=time;
        this.venue=venue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInAttendanceID() {
        return inAttendanceID;
    }

    public void setInAttendanceID(Long inAttendanceID) {
        this.inAttendanceID = inAttendanceID;
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
}

