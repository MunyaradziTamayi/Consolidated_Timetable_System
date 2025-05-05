package com.app.timetable.Controller;

import com.app.timetable.Entity.ChiefInvigilator;
import com.app.timetable.Helpers.StringConverter;
import com.app.timetable.Entity.Administrator;
import com.app.timetable.Service.AdministratorService;
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
@RequestMapping("/administrators")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @GetMapping("")
    public String getAllAdministrators(Model model) {
        var list = administratorService.getAllAdministrators();
        model.addAttribute("administrators", list);
        return "administrators.html";
    }

    @PostMapping("/add_administrator")
    public RedirectView createAdministrator( @ModelAttribute Administrator administrator) {
        Administrator newAdministrator = new Administrator(StringConverter.toCamelCase(administrator.getName()), StringConverter.toCamelCase(administrator.getSurname()),administrator.getEmail());
        administratorService.createAdministrator(newAdministrator);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/administrators");
        return redirectView;
    }

    @PostMapping("/update/{id}")
    public RedirectView updateAdministrator(@PathVariable String id, @ModelAttribute Administrator administrator) {
        Administrator newAdministrator = new Administrator(StringConverter.toCamelCase(administrator.getName()), StringConverter.toCamelCase(administrator.getSurname()),administrator.getEmail());
        administratorService.updateAdministrator(Long.parseLong(id), newAdministrator);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/administrators");
        return redirectView;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/administrators");
        return redirectView;
    }

    @PostMapping("/upload")
    public RedirectView mapReapExcelDataToDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i=0;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            Administrator administrator = new Administrator();

            XSSFRow row = worksheet.getRow(i);

            administrator.setName(row.getCell(0).getStringCellValue());
            administrator.setSurname(row.getCell(1).getStringCellValue());
            administrator.setEmail(row.getCell(2).getStringCellValue());

            administratorService.createAdministrator(administrator);

        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/administrators");
        return redirectView;
    }


}
