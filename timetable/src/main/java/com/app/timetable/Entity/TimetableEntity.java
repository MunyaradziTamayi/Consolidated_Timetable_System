package com.app.timetable.Entity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimetableEntity {

    private String date;
    private String time;
    private String exam;
    private String invigilators;
    private String assistantInvigilator;
    private String groupAllocation;
    private String venue;
    private String administrator;
    private String inAttendances;
    private String chiefInvigilator;
    private String examID;

    public TimetableEntity() {
        // Default constructor
    }

    public TimetableEntity(String date, String time, String exam, String invigilators, String assistantInvigilator, String groupAllocation, String venue, String administrator, String inAttendances, String chiefInvigilator, String examID) {

        this.date = date;
        this.time = time;
        this.exam = exam;
        this.invigilators = invigilators;
        this.assistantInvigilator = assistantInvigilator;
        this.groupAllocation = groupAllocation;
        this.venue = venue;
        this.administrator = administrator;
        this.inAttendances = inAttendances;
        this.chiefInvigilator = chiefInvigilator;
        this.examID = examID;

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

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getInvigilators() {
        return invigilators;
    }

    public void setInvigilators(String invigilators) {
        this.invigilators = invigilators;
    }

    public String getAssistantInvigilator() {
        return assistantInvigilator;
    }

    public void setAssistantInvigilator(String assistantInvigilator) {
        this.assistantInvigilator = assistantInvigilator;
    }

    public String getGroupAllocation() {
        return groupAllocation;
    }

    public void setGroupAllocation(String groupAllocation) {
        this.groupAllocation = groupAllocation;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public String getInAttendances() {
        return inAttendances;
    }

    public void setInAttendances(String inAttendances) {
        this.inAttendances = inAttendances;
    }

    public String getChiefInvigilator() {
        return chiefInvigilator;
    }

    public void setChiefInvigilator(String chiefInvigilator) {
        this.chiefInvigilator = chiefInvigilator;
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public Date getExamDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        return formatter.parse(getDate());
    }
}

