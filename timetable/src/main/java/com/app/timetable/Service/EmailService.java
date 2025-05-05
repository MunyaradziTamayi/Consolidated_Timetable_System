package com.app.timetable.Service;

import com.app.timetable.Entity.*;
import com.app.timetable.Entity.RoasterEntity.InAttendanceRoaster;
import com.app.timetable.Entity.RoasterEntity.InvigilatorRoaster;
import com.app.timetable.Helpers.PDFService;
import com.app.timetable.Helpers.StaffTimetableHTMLGenerator;
import com.app.timetable.Helpers.TimetableHTMLGenerator;
import com.app.timetable.Interface.AdministratorRepository;
import com.app.timetable.Interface.ChiefInvigilatorRepository;
import com.app.timetable.Interface.InAttendanceRepository;
import com.app.timetable.Interface.InvigilatorRepository;
import com.app.timetable.Service.RoasterService.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InvigilatorRepository invigilatorRepository;

    @Autowired
    private ExamService examService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ChiefInvigilatorService chiefInvigilatorService;

    @Autowired
    PDFService pdfService;

    @Autowired
    private InvigilatorService invigilatorService;

    @Autowired
    private InAttendanceService inAttendanceService;

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
    private InAttendanceRepository inAttendanceRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private ChiefInvigilatorRepository chiefInvigilatorRepository;


    public void sendEmailsToAll() {

        sendToInvigilators();
        sendToAdministrators();
        sendToChiefInvigilators();
        sendToSupportingStaff();

    }

    private void sendToInvigilators() {
        List<Invigilator> invigilators = (List<Invigilator>) invigilatorRepository.findAll();

        for (Invigilator invigilator : invigilators) {
            try {
                String to = invigilator.getEmail();
                String subject = "Invigilation schedule for" + invigilator.getName() + " " + invigilator.getSurname();

                ArrayList<Exam> exams = new ArrayList<>();

                List<InvigilatorRoaster> roasters = invigilatorRoasterService.getAllInvigilatorRoasters().stream().filter(var -> var.getInvigilatorID().equals(invigilator.getId())).toList();

                for (InvigilatorRoaster invigilatorRoaster : roasters) {
                    exams.add(examService.getExamById(invigilatorRoaster.getExam()));
                }

                String path = getPDFPath(invigilator.getName() + invigilator.getSurname(), exams);

                //sendEmail(to, subject, path);

            } catch (DocumentException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendToSupportingStaff() {
        List<InAttendance> inAttendances = (List<InAttendance>) inAttendanceRepository.findAll();

        for (InAttendance inAttendance : inAttendances) {
            try {
                String to = inAttendance.getEmail();
                String subject = "Supporting staff schedule for" + inAttendance.getName() + " " + inAttendance.getSurname();

                ArrayList<StaffTimetableEntity> staffTimetableEntities = new ArrayList<>();

                inattendanceRoasterService.getAllInAttendanceRoasters().stream()
                        .filter(i -> Objects.equals(i.getInAttendanceID(), inAttendance.getId())).toList()
                        .forEach(i -> {
                            staffTimetableEntities.add(new StaffTimetableEntity(i.getDate(),i.getTime(),i.getVenue(),inAttendance.getName() + " " + inAttendance.getSurname()));
                        });

                String path = getStaffPDFPath(inAttendance.getName() + inAttendance.getSurname(), staffTimetableEntities, "Supporting staff");

                sendEmail(to, subject, path);

            } catch (DocumentException | IOException | MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendToAdministrators() {

        List<Administrator> administrators = (List<Administrator>) administratorRepository.findAll();

        for (Administrator administrator : administrators) {
            try {
                String to = administrator.getEmail();
                String subject = "Administrator schedule for" + administrator.getName() + " " + administrator.getSurname();

                ArrayList<StaffTimetableEntity> staffTimetableEntities = new ArrayList<>();

                administratorRoasterService.getAllAdministratorRoasters().stream()
                        .filter(i -> Objects.equals(i.getAdministratorID(), administrator.getId())).toList()
                        .forEach(i -> {
                            staffTimetableEntities.add(new StaffTimetableEntity(i.getDate(),i.getTime(),i.getVenue(),administrator.getName() + " " + administrator.getSurname()));
                        });

                String path = getStaffPDFPath(administrator.getName() + administrator.getSurname(), staffTimetableEntities, "Administrator");

                //sendEmail(to, subject, path);

            } catch (DocumentException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void sendToChiefInvigilators() {

        List<ChiefInvigilator> chiefInvigilators = (List<ChiefInvigilator>) chiefInvigilatorRepository.findAll();

        for (ChiefInvigilator chiefInvigilator : chiefInvigilators) {
            try {
                String to = chiefInvigilator.getEmail();
                String subject = "Chief Invigilator schedule for" + chiefInvigilator.getName() + " " + chiefInvigilator.getSurname();

                ArrayList<StaffTimetableEntity> staffTimetableEntities = new ArrayList<>();

                chiefInvigilatorRoasterService.getAllChiefInvigilatorRoasters().stream()
                        .filter(i -> Objects.equals(i.getChiefInvigilatorID(), chiefInvigilator.getId())).toList()
                        .forEach(i -> {
                            staffTimetableEntities.add(new StaffTimetableEntity(i.getDate(),i.getTime(),i.getVenue(),chiefInvigilator.getName() + " " + chiefInvigilator.getSurname()));
                        });

                String path = getStaffPDFPath(chiefInvigilator.getName() + chiefInvigilator.getSurname(), staffTimetableEntities, "Chief Invigilators");

                //sendEmail(to, subject, path);

            } catch (DocumentException | IOException  /*|MessagingException*/ e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void sendEmail(String to, String subject, String attachmentPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Personalised exam timetable");

        FileSystemResource file = new FileSystemResource(new File(attachmentPath));
        helper.addAttachment(file.getFilename(), file);

        mailSender.send(message);
    }

    private String getPDFPath(String name, ArrayList<Exam> exams) throws DocumentException, IOException {


        byte[] bytes = pdfService.generatePDFFromHTML(TimetableHTMLGenerator.generateHTML(getTimetableEntities(exams), "Generated timetable"));

        String path = System.getProperty("user.home") + "/Desktop/Uploads" + File.separator + name + ".pdf";
        try {

            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
        } catch (Exception e) {

            System.out.println("Exception: " + e);
        }

        return path;

    }

    private String getStaffPDFPath(String name, ArrayList<StaffTimetableEntity> staffTimetableEntities, String staffHeading) throws DocumentException, IOException {


        byte[] bytes = pdfService.generatePDFFromHTML(StaffTimetableHTMLGenerator.generateHTML(staffTimetableEntities, "Generated timetable", staffHeading));

        String path = System.getProperty("user.home") + "/Desktop/Uploads" + File.separator + name + ".pdf";
        try {

            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
        } catch (Exception e) {

            System.out.println("Exception: " + e);
        }

        return path;

    }


    private ArrayList<TimetableEntity> getTimetableEntities(List<Exam> exams) {

        TimetableService timetableService = new TimetableService(

                exams,
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

        ArrayList<TimetableEntity> timetable = timetableService.getTimetable();

        timetable.sort(Comparator.comparing(TimetableEntity::getVenue));
        timetable.sort(Comparator.comparing(TimetableEntity::getTime));

        timetable.sort((t1, t2) -> {
            try {
                return t1.getExamDate().compareTo(t2.getExamDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        return timetable;
    }


}

