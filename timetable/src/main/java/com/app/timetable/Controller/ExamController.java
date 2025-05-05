package com.app.timetable.Controller;


import com.app.timetable.Entity.RoasterEntity.InAttendanceRoaster;
import com.app.timetable.Entity.RoasterEntity.InvigilatorRoaster;
import com.app.timetable.Helpers.FileService;
import com.app.timetable.Helpers.MultiPartFileToFileConverter;
import com.app.timetable.Helpers.PDFService;
import com.app.timetable.Entity.*;
import com.app.timetable.Service.*;
import com.app.timetable.Service.RoasterService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


@Controller
@RequestMapping("/exams")
public class ExamController {

    private static final String INVIGILATOR_TYPE = "Invigilators";
    private static final String IN_ATTENDANCE_TYPE = "In-attendance";
    private static final String ADMINISTRATOR_TYPE = "Administrators";

    @Autowired
    PDFService pdfService;

    @Autowired
    private EmailService emailService;


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
    private InAttendanceRoasterService inattendanceRoasterService;


    @Autowired
    private AdministratorRoasterService administratorRoasterService;

    @Autowired
    private ChiefInvigilatorRoasterService chiefInvigilatorRoasterService;

    @Autowired
    private AssistantInvigilatorRoasterService assistantInvigilatorRoasterService;





    @GetMapping("")
    public String getAllExams(Model model) {

        var exams = examService.getAllExams();
        model.addAttribute("exams", exams);

        return "exams.html";

    }

    @PostMapping("/add_exam")
    public RedirectView createExam(@ModelAttribute Exam exam) {
        examService.createExam(exam);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/exams");
        return redirectView;
    }

    @PostMapping("/update/{id}")
    public RedirectView updateExam(@PathVariable Long id, @ModelAttribute Exam exam) {

        examService.updateExam(id, exam);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/exams");
        return redirectView;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/exams");
        return redirectView;
    }

    @GetMapping("/send-emails")
    public RedirectView sendEmails() {
        
        emailService.sendEmailsToAll();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/exams");
        return redirectView;
    }

    @PostMapping("/uploadPDF")
    public RedirectView handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        ArrayList<Exam> data= FileService.getExamListFromPDF(MultiPartFileToFileConverter.convert(file));
        for (Exam exam : data) {
            examService.createExam(exam);
        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/exams");
        return redirectView;

    }


    private Map<String, Long> getType(String email) {

        var invigilators = invigilatorService.getAllInvigilators();
        var invigilator = invigilators.stream().filter(var -> var.getEmail().equals(email)).findFirst().orElse(null);

        var inAttendances = inAttendanceService.getAllInAttendance();
        var inAttendance = inAttendances.stream().filter(var -> var.getEmail().equals(email)).findFirst().orElse(null);

        var admins = administratorService.getAllAdministrators();
        var admin = admins.stream().filter(var -> var.getEmail().equals(email)).findFirst().orElse(null);



        Map<String, Long> map = new HashMap<>();

        if (invigilator != null) {
            map.put(INVIGILATOR_TYPE, invigilator.getId());
            return map;
        } else if (inAttendance != null) {
            map.put(IN_ATTENDANCE_TYPE, inAttendance.getId());
            return map;
        } else if (admin != null) {
            map.put(ADMINISTRATOR_TYPE, admin.getId());
            return map;
        } else {
            return null;
        }

    }
    private ArrayList<TimetableEntity> getTimetableEntities() {

        TimetableService timetableService=new TimetableService(

                examService.getAllExams(),
                invigilatorService.getAllInvigilators(),
                inAttendanceService.getAllInAttendance(),
                administratorService.getAllAdministrators(),
                chiefInvigilatorService.getAllChiefInvigilators(),
                invigilatorRoasterService.getAllInvigilatorRoasters(),
                inattendanceRoasterService.getAllInAttendanceRoasters(),
                administratorRoasterService.getAllAdministratorRoasters(),
                chiefInvigilatorRoasterService.getAllChiefInvigilatorRoasters(),
                assistantInvigilatorRoasterService.getAllAssistantInvigilatorRoasters()

        );

        ArrayList<TimetableEntity> timetable=timetableService.getTimetable();

        timetable.sort(Comparator.comparing(TimetableEntity::getVenue));
        timetable.sort(Comparator.comparing(TimetableEntity::getTime));

        timetable.sort((t1,t2)-> {
            try {
                return t1.getExamDate().compareTo(t2.getExamDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        return timetable;
    }


}
