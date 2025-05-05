package com.app.timetable.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chief_invigilators")
public class ChiefInvigilator {

    // Getters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;

    public ChiefInvigilator() {
        // Default constructor
    }

    public ChiefInvigilator(String name, String surname, String email) {

        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

}

