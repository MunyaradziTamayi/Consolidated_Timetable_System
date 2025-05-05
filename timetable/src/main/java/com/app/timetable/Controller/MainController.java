package com.app.timetable.Controller;

import com.app.timetable.Entity.RoasterEntity.InAttendanceRoaster;
import com.app.timetable.Entity.RoasterEntity.InvigilatorRoaster;
import com.app.timetable.Helpers.*;
import com.app.timetable.Entity.*;
import com.app.timetable.Exceptions.InvalidNumberOfInAttendancesException;
import com.app.timetable.Exceptions.InvalidNumberOfInvigilatorsException;
import com.app.timetable.Service.*;
import com.app.timetable.Service.RoasterService.*;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;


@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    PDFService pdfService;

    @Autowired
    private InvigilatorService invigilatorService;

    @Autowired
    private ExamService examService;

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

    @Autowired
    StaffAssigner staffAssigner;


    @GetMapping("/")
    public String getDefault() {
        return "landing_page.html";
    }

    @GetMapping("/home")
    public String getHome(Model model) {

        model.addAttribute("timetableEntities", getTimetableEntities());
        return "home.html";
    }

    @GetMapping(value = "/generate_pdf")
    public ResponseEntity<byte[]> convertToPDF() throws DocumentException, IOException {

        byte[] bytes = pdfService.generatePDFFromHTML(TimetableHTMLGenerator.generateHTML(getTimetableEntities(), "Generated timetable"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "output.pdf");
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }


    @GetMapping("/assign_staff")
    public RedirectView assignStaff() {

        staffAssigner.assignAllStaff();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return redirectView;

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
