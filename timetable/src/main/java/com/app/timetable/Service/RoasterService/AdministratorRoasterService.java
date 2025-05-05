package com.app.timetable.Service.RoasterService;

import com.app.timetable.Entity.RoasterEntity.AdministratorRoaster;
import com.app.timetable.Entity.RoasterEntity.InAttendanceRoaster;
import com.app.timetable.Interface.RoasterInterface.AdministratorRoasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorRoasterService {

    @Autowired
    private AdministratorRoasterRepository administratorRoasterRepository;

    public List<AdministratorRoaster> getAllAdministratorRoasters() {
        return (List<AdministratorRoaster>) administratorRoasterRepository.findAll();
    }

    public AdministratorRoaster getAdministratorRoasterById(Long id) {

        return administratorRoasterRepository.findById(id).orElse(null);
    }

    public AdministratorRoaster createAdministratorRoaster(AdministratorRoaster administratorRoaster) {
        return administratorRoasterRepository.save(administratorRoaster);
    }

    public void updateAdministratorRoaster(Long id, AdministratorRoaster administratorRoaster) {
        AdministratorRoaster existingAdministratorRoaster = administratorRoasterRepository.findById(id).orElse(null);
        if (existingAdministratorRoaster != null) {
            existingAdministratorRoaster.setAdministratorID(administratorRoaster.getAdministratorID());
            existingAdministratorRoaster.setDate(administratorRoaster.getDate());
            existingAdministratorRoaster.setTime(administratorRoaster.getTime());
            existingAdministratorRoaster.setVenue(administratorRoaster.getVenue());
        }
    }

    public void deleteAdministratorRoaster(Long id) {
        administratorRoasterRepository.deleteById(id);
    }

    public void deleteAll(){
        administratorRoasterRepository.deleteAll();
    }

}


