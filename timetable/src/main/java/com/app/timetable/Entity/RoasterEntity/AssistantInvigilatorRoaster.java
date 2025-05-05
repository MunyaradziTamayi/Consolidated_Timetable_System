package com.app.timetable.Entity.RoasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "assistant_invigilator_roaster")
public class AssistantInvigilatorRoaster {

    // Getters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long invigilatorID;
    private String date;
    private String time;
    private String venue;

    public AssistantInvigilatorRoaster() {
        // Default constructor
    }

    public AssistantInvigilatorRoaster(Long invigilatorID,String date,String time,String venue) {

        this.invigilatorID=invigilatorID;
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

    public Long getInvigilatorID() {
        return invigilatorID;
    }

    public void setInvigilatorID(Long invigilatorID) {
        this.invigilatorID = invigilatorID;
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

