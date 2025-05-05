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
@Table(name = "chief_invigilator_roaster")
public class ChiefInvigilatorRoaster {

    // Getters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chiefInvigilatorID;
    private String date;
    private String time;
    private String venue;

    public ChiefInvigilatorRoaster() {
        // Default constructor
    }

    public ChiefInvigilatorRoaster(Long chiefInvigilatorID,String date,String time,String venue) {

        this.chiefInvigilatorID=chiefInvigilatorID;
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

    public Long getChiefInvigilatorID() {
        return chiefInvigilatorID;
    }

    public void setChiefInvigilatorID(Long chiefInvigilatorID) {
        this.chiefInvigilatorID = chiefInvigilatorID;
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

