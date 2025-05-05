package com.app.timetable.Service;

import com.app.timetable.Entity.*;
import com.app.timetable.Entity.RoasterEntity.*;
import java.util.*;
import java.util.stream.Collectors;


public class TimetableService {


    private final ArrayList<Exam> exams = new ArrayList<>();
    private final ArrayList<Invigilator> invigilators = new ArrayList<>();
    private final ArrayList<InAttendance> inAttendances = new ArrayList<>();
    private final ArrayList<Administrator> administrators = new ArrayList<>();
    private final ArrayList<ChiefInvigilator> chiefInvigilators = new ArrayList<>();

    private final ArrayList<InvigilatorRoaster> invigilatorRoasters = new ArrayList<>();
    private final ArrayList<InAttendanceRoaster> inAttendanceRoasters = new ArrayList<>();
    private final ArrayList<AdministratorRoaster> administratorRoasters = new ArrayList<>();
    private final ArrayList<ChiefInvigilatorRoaster> chiefInvigilatorRoasters = new ArrayList<>();
    private final ArrayList<AssistantInvigilatorRoaster> assistantInvigilatorRoasters = new ArrayList<>();

    public TimetableService(
            List<Exam> exams,
            List<Invigilator> invigilators,
            List<InAttendance> inAttendances,
            List<Administrator> administrators,
            List<ChiefInvigilator> chiefInvigilators,
            List<InvigilatorRoaster> invigilatorRoasters,
            List<InAttendanceRoaster> inAttendanceRoasters,
            List<AdministratorRoaster> administratorRoasters,
            List<ChiefInvigilatorRoaster> chiefInvigilatorRoasters,
            List<AssistantInvigilatorRoaster> assistantInvigilatorRoasters
                            ) {
        this.exams.addAll(exams);
        this.invigilators.addAll(invigilators);
        this.inAttendances.addAll(inAttendances);
        this.administrators.addAll(administrators);
        this.chiefInvigilators.addAll(chiefInvigilators);
        this.invigilatorRoasters.addAll(invigilatorRoasters);
        this.inAttendanceRoasters.addAll(inAttendanceRoasters);
        this.administratorRoasters.addAll(administratorRoasters);
        this.chiefInvigilatorRoasters.addAll(chiefInvigilatorRoasters);
        this.assistantInvigilatorRoasters.addAll(assistantInvigilatorRoasters);
    }


    public ArrayList<TimetableEntity> getTimetable() {

        ArrayList<TimetableEntity> timetable = new ArrayList<>();

        Map<String, List<Exam>> groupedByDate = exams.stream().collect(Collectors.groupingBy(Exam::getDate));

        groupedByDate.forEach((date, dValue) -> {
            Map<String, List<Exam>> groupedByTime = dValue.stream().collect(Collectors.groupingBy(Exam::getTime));
            groupedByTime.forEach((time, tValue) -> {
                Map<String, List<Exam>> groupedByVenue = tValue.stream().collect(Collectors.groupingBy(Exam::getVenue));
                groupedByVenue.forEach((venue, vValue) -> {

                    for (Exam exam : vValue) {

                        timetable.add(new TimetableEntity(
                                date,
                                time,
                                exam.getCourseCode(),
                                getInvigilators(exam),
                                getAssistantInvigilators(date, time, venue),
                                exam.getNumOfStudents(),
                                exam.getVenue(),
                                getAdministrators(date, time, venue),
                                getInAttendances(date, time, venue),
                                getChiefInvigilators(date, time, venue),
                                exam.getId().toString()
                        ));

                    }


                });
            });
        });

        return timetable;
    }

    private String getInvigilators(Exam exam) {

        ArrayList<String> invigilators = new ArrayList<>();
        invigilatorRoasters.stream().filter(a -> a.getExam().equals(exam.getId())).forEach(a -> {
            invigilators.add(getInvigilatorName(a.getInvigilatorID()));
        });

        StringBuilder list = new StringBuilder();
        for (String invigilator : invigilators) {
            list.append(invigilator).append("\n");
        }

        return list.toString();

    }

    private String getInvigilatorName(Long id) {
        Invigilator invigilator = invigilators.stream().filter(a-> a.getId().equals(id)).findFirst().orElse(null);
        if(invigilator!=null) {
            return invigilator.getName() + " " + invigilator.getSurname();
        }else {
            return "";
        }
    }

    private String getInAttendances(String date, String time, String venue) {

        ArrayList<String> staffMembers = new ArrayList<>();
        inAttendanceRoasters.stream().filter(
                a -> a.getDate().equals(date) &&
                        a.getTime().equals(time) &&
                        a.getVenue().equals(venue)
        ).forEach(a -> {
            staffMembers.add(getInAttendanceName(a.getInAttendanceID()));
        });

        StringBuilder list = new StringBuilder();
        for (String staff : staffMembers) {
            list.append(staff).append("\n");
        }

        return list.toString();

    }

    private String getInAttendanceName(Long id) {
        InAttendance inAttendance = inAttendances.stream().filter(a->a.getId().equals(id)).findFirst().orElse(null);
        if(inAttendance!=null) {
            return inAttendance.getName() + " " + inAttendance.getSurname();
        }else {
            return "";
        }
    }


    private String getAdministrators(String date, String time, String venue) {

        ArrayList<String> staffMembers = new ArrayList<>();
        administratorRoasters.stream().filter(
                a -> a.getDate().equals(date) &&
                        a.getTime().equals(time) &&
                        a.getVenue().equals(venue)
        ).forEach(a -> {
            staffMembers.add(getAdministratorName(a.getAdministratorID()));
        });

        StringBuilder list = new StringBuilder();
        for (String staff : staffMembers) {
            list.append(staff).append("\n");
        }

        return list.toString();

    }

    private String getAdministratorName(Long id) {
        Administrator administrator = administrators.stream().filter(a->a.getId().equals(id)).findFirst().orElse(null);
        if(administrator!=null) {
            return administrator.getName() + " " + administrator.getSurname();
        }else {
            return "";
        }
    }

    private String getChiefInvigilators(String date, String time, String venue) {

        ArrayList<String> staffMembers = new ArrayList<>();
        chiefInvigilatorRoasters.stream().filter(
                a -> a.getDate().equals(date) &&
                        a.getTime().equals(time) &&
                        a.getVenue().equals(venue)
        ).forEach(a -> {
            staffMembers.add(getChiefInvigilatorName(a.getChiefInvigilatorID()));
        });

        StringBuilder list = new StringBuilder();
        for (String staff : staffMembers) {
            list.append(staff).append("\n");
        }

        return list.toString();

    }

    private String getChiefInvigilatorName(Long id) {
        ChiefInvigilator chiefInvigilator=chiefInvigilators.stream().filter(a->a.getId().equals(id)).findFirst().orElse(null);
        if(chiefInvigilator!=null) {
            return chiefInvigilator.getName() + " " + chiefInvigilator.getSurname();
        }else {
            return "";
        }

    }

    private String getAssistantInvigilators(String date, String time, String venue) {
        ArrayList<String> staffMembers = new ArrayList<>();
        assistantInvigilatorRoasters.stream().filter(
                a -> a.getDate().equals(date) &&
                        a.getTime().equals(time) &&
                        a.getVenue().equals(venue)
        ).forEach(a -> {
            staffMembers.add(getInvigilatorName(a.getInvigilatorID()));
        });

        StringBuilder list = new StringBuilder();
        for (String staff : staffMembers) {
            list.append(staff).append("\n");
        }

        return list.toString();
    }

}
