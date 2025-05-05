package com.app.timetable.Controller;

import com.app.timetable.Entity.ChiefInvigilator;
import com.app.timetable.Entity.Invigilator;
import com.app.timetable.Helpers.StringConverter;
import com.app.timetable.Entity.Administrator;
import com.app.timetable.Service.ChiefInvigilatorService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequestMapping("/chief_invigilators")
public class ChiefInvigilatorController {

    @Autowired
    private ChiefInvigilatorService chiefInvigilatorService;

    @GetMapping("")
    public String getAllChiefInvigilators(Model model) {
        var list = chiefInvigilatorService.getAllChiefInvigilators();
        model.addAttribute("chiefInvigilators", list);
        return "chiefInvigilators.html";
    }

    @PostMapping("/add_chief_invigilator")
    public RedirectView createChiefInvigilator( @ModelAttribute ChiefInvigilator chiefInvigilator) {
        ChiefInvigilator newChiefInvigilator = new ChiefInvigilator(StringConverter.toCamelCase(chiefInvigilator.getName()), StringConverter.toCamelCase(chiefInvigilator.getSurname()),chiefInvigilator.getEmail());
        chiefInvigilatorService.createChiefInvigilator(newChiefInvigilator);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/chief_invigilators");
        return redirectView;
    }

    @PostMapping("/update/{id}")
    public RedirectView updateChiefInvigilator(@PathVariable String id, @ModelAttribute ChiefInvigilator chiefInvigilator) {
        ChiefInvigilator newChiefInvigilator = new ChiefInvigilator(StringConverter.toCamelCase(chiefInvigilator.getName()), StringConverter.toCamelCase(chiefInvigilator.getSurname()),chiefInvigilator.getEmail());
        chiefInvigilatorService.updateChiefInvigilator(Long.parseLong(id), newChiefInvigilator);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/chief_invigilators");
        return redirectView;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteChiefInvigilator(@PathVariable Long id) {
        chiefInvigilatorService.deleteChiefInvigilator(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/chief_invigilators");
        return redirectView;
    }

    @PostMapping("/upload")
    public RedirectView mapReapExcelDataToDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i=0;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            ChiefInvigilator chiefInvigilator = new ChiefInvigilator();

            XSSFRow row = worksheet.getRow(i);

            chiefInvigilator.setName(row.getCell(0).getStringCellValue());
            chiefInvigilator.setSurname(row.getCell(1).getStringCellValue());
            chiefInvigilator.setEmail(row.getCell(2).getStringCellValue());

            chiefInvigilatorService.createChiefInvigilator(chiefInvigilator);

        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/chief_invigilators");
        return redirectView;
    }


}
