package com.app.timetable.Entity.RoasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "administrator_roaster")
public class AdministratorRoaster {

    // Getters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long administratorID;
    private String date;
    private String time;
    private String venue;

    public AdministratorRoaster() {
        // Default constructor
    }

    public AdministratorRoaster(Long administratorID,String date,String time,String venue) {

        this.administratorID=administratorID;
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

    public Long getAdministratorID() {
        return administratorID;
    }

    public void setAdministratorID(Long administratorID) {
        this.administratorID = administratorID;
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

