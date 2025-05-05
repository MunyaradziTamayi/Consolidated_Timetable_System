package com.app.timetable.Helpers;

import com.app.timetable.Entity.Exam;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileService {

    public static ArrayList<Exam> getExamListFromPDF(File file) throws IOException {
        ArrayList<Exam> exams = new ArrayList<>();

        PDDocument document = Loader.loadPDF(file);

        PDFTableStripper stripper = new PDFTableStripper();
        stripper.setSortByPosition(true);


        for (int page = 0; page < document.getNumberOfPages(); ++page) {

            PDPage pdPage = document.getPage(page);

            stripper.extractTable(pdPage);

            for (int r = 0; r < stripper.getRows(); r++) {

                Exam exam = new Exam(
                        stripper.getText(r, 0).trim(),
                        stripper.getText(r, 1).trim(),
                        stripper.getText(r, 2).trim(),
                        stripper.getText(r, 3).trim(),
                        stripper.getText(r, 4).trim(),
                        stripper.getText(r, 5).trim(),
                        stripper.getText(r, 6).trim(),
                        stripper.getText(r, 7).trim()
                );

                if (!exam.getNumOfStudents().trim().isEmpty()&&isNumeric(exam.getNumOfStudents().trim())) {
                    exams.add(exam);
                }

            }
        }

        return exams;

    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
