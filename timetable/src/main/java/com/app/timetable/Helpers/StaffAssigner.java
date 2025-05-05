package com.app.timetable.Helpers;

import com.app.timetable.Entity.*;
import com.app.timetable.Entity.RoasterEntity.*;
import com.app.timetable.Service.*;
import com.app.timetable.Service.RoasterService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffAssigner {

    @Autowired
    private ExamService examService;

    @Autowired
    private InvigilatorService invigilatorService;
    @Autowired
    private InAttendanceService inAttendanceService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private ChiefInvigilatorService chiefInvigilatorService;

    @Autowired
    private InvigilatorRoasterService invigilatorRoasterService;
    @Autowired
    private InAttendanceRoasterService inAttendanceRoasterService;
    @Autowired
    private AdministratorRoasterService administratorRoasterService;
    @Autowired
    private ChiefInvigilatorRoasterService chiefInvigilatorRoasterService;
    @Autowired
    private AssistantInvigilatorRoasterService assistantInvigilatorRoasterService;


    private final ArrayList<Exam> exams = new ArrayList<>();

    private final ArrayList<Invigilator> invigilators = new ArrayList<>();
    private final ArrayList<InAttendance> inAttendances = new ArrayList<>();
    private final ArrayList<Administrator> administrators = new ArrayList<>();
    private final ArrayList<ChiefInvigilator> chiefInvigilators = new ArrayList<>();

    private static final int maxCount = 30;
    private static final int inAttendanceMaxCount = 100;

    int invigilatorOffset = 0;
    int inAttendanceOffset = 0;
    int adminOffset = 0;
    int chiefInvigilatorOffset = 0;

    public void assignAllStaff() {

        exams.addAll(examService.getAllExams());

        invigilators.addAll(invigilatorService.getAllInvigilators());
        inAttendances.addAll(inAttendanceService.getAllInAttendance());
        administrators.addAll(administratorService.getAllAdministrators());
        chiefInvigilators.addAll(chiefInvigilatorService.getAllChiefInvigilators());

        invigilatorRoasterService.deleteAll();
        inAttendanceRoasterService.deleteAll();
        administratorRoasterService.deleteAll();
        chiefInvigilatorRoasterService.deleteAll();

        Collections.shuffle(this.invigilators);

        Map<String, List<Exam>> groupedByDate = exams.stream().collect(Collectors.groupingBy(Exam::getDate));
        groupedByDate.forEach((dKey, dValue) -> {
            Map<String, List<Exam>> groupedByTime = dValue.stream().collect(Collectors.groupingBy(Exam::getTime));
            groupedByTime.forEach((tKey, tValue) -> {
                Map<String, List<Exam>> groupedByVenue = tValue.stream().collect(Collectors.groupingBy(Exam::getVenue));
                groupedByVenue.forEach((vKey, vValue) -> {

                    assignInvigilators(vValue);
                    assignAdmin(dKey, tKey, vKey);
                    assignChiefInvigilator(dKey, tKey, vKey);
                    assignInAttendance(dKey, tKey, vKey, getExamsTotalCount(vValue));
                    assignAssistantInvigilator(dKey, tKey, vKey);

                });
            });
        });

    }

    private void assignInvigilators(List<Exam> exams) {
        ArrayList<Exam> lessThanMax = new ArrayList<>();

        for (Exam exam : exams) {

            if (Integer.parseInt(exam.getNumOfStudents()) >= maxCount) {
                assignExam(exam);
            } else {
                lessThanMax.add(exam);
            }
        }
        assignGroupedExam(lessThanMax);
    }

    private void assignExam(Exam exam) {
        int count = Math.floorDiv(Integer.parseInt(exam.getNumOfStudents()), maxCount) + 1;
        for (int i = 0; i < count; i++) {
            Invigilator invigilator = invigilators.get(invigilatorOffset);
            invigilatorRoasterService.createInvigilatorRoaster(
                    new InvigilatorRoaster(exam.getId(), invigilator.getId())
            );
            if (invigilatorOffset == invigilators.size() - 1) {
                invigilatorOffset = 0;
            } else {
                invigilatorOffset++;
            }
        }
    }

    private void assignGroupedExam(ArrayList<Exam> lessThanMax) {

        Map<String, ArrayList<Exam>> map = getExamMap(lessThanMax);
        map.forEach((k, v) -> {
            for (Exam exam : v) {
                Invigilator invigilator = invigilators.get(invigilatorOffset);
                invigilatorRoasterService.createInvigilatorRoaster(
                        new InvigilatorRoaster(exam.getId(), invigilator.getId())
                );
            }
            if (invigilatorOffset == invigilators.size() - 1) {
                invigilatorOffset = 0;
            } else {
                invigilatorOffset++;
            }
        });

    }

    private Map<String, ArrayList<Exam>> getExamMap(ArrayList<Exam> lessThanMax) {
        Map<String, ArrayList<Exam>> map = new HashMap<>();

        ArrayList<Exam> desc = new ArrayList<>(lessThanMax);

        ArrayList<Exam> assigned = new ArrayList<>();

        desc.sort(Comparator.comparingInt(Exam::getNumericNumOfStudents).reversed());

        for (int i = 0; i < desc.size(); i++) {

            if (!assigned.contains(desc.get(i))) {

                ArrayList<Exam> groupedExam = new ArrayList<>();
                groupedExam.add(desc.get(i));
                assigned.add(desc.get(i));

                int total = desc.get(i).getNumericNumOfStudents();

                for (int j = desc.size() - 1; j >= 0; j--) {
                    if (!assigned.contains(desc.get(j))) {
                        if(total+desc.get(j).getNumericNumOfStudents()<maxCount) {
                            total+=desc.get(j).getNumericNumOfStudents();
                            groupedExam.add(desc.get(j));
                            assigned.add(desc.get(j));
                        }else{
                            break;
                        }
                    }

                }

                map.put(Integer.toString(i), groupedExam);
            }

        }


        return map;
    }


    private void assignAdmin(String date, String time, String venue) {

        AdministratorRoaster administratorRoaster = new AdministratorRoaster(administrators.get(adminOffset).getId(), date, time, venue);
        administratorRoasterService.createAdministratorRoaster(administratorRoaster);

        if (adminOffset == administrators.size() - 1) {
            adminOffset = 0;
        } else {
            adminOffset++;
        }

    }

    private void assignChiefInvigilator(String date, String time, String venue) {

        ChiefInvigilatorRoaster chiefInvigilatorRoaster = new ChiefInvigilatorRoaster(chiefInvigilators.get(chiefInvigilatorOffset).getId(), date, time, venue);
        chiefInvigilatorRoasterService.createChiefInvigilatorRoaster(chiefInvigilatorRoaster);

        if (chiefInvigilatorOffset == chiefInvigilators.size() - 1) {
            chiefInvigilatorOffset = 0;
        } else {
            chiefInvigilatorOffset++;
        }

    }

    private void assignAssistantInvigilator(String date, String time, String venue) {

        AssistantInvigilatorRoaster assistantInvigilatorRoaster = new AssistantInvigilatorRoaster(invigilators.get(invigilatorOffset).getId(), date, time, venue);
        assistantInvigilatorRoasterService.createAssistantInvigilatorRoaster(assistantInvigilatorRoaster);

        if (invigilatorOffset == invigilators.size() - 1) {
            invigilatorOffset = 0;
        } else {
            invigilatorOffset++;
        }

    }

    private void assignInAttendance(String date, String time, String venue, int count) {

        for (int i = 0; i < Math.floorDiv(count, inAttendanceMaxCount) + 1; i++) {
            inAttendanceRoasterService.createInAttendanceRoaster(
                    new InAttendanceRoaster(
                            inAttendances.get(inAttendanceOffset).getId(),
                            date,
                            time,
                            venue
                    )
            );
            if (inAttendanceOffset == inAttendances.size() - 1) {
                inAttendanceOffset = 0;
            } else {
                inAttendanceOffset++;
            }
        }

    }


    private int getExamsTotalCount(List<Exam> examList) {
        int total = 0;
        for (Exam exam : examList) {
            total += Integer.parseInt(exam.getNumOfStudents());
        }

        return total;
    }


}
