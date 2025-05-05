package com.app.timetable.Service.RoasterService;

import com.app.timetable.Entity.RoasterEntity.AdministratorRoaster;
import com.app.timetable.Entity.RoasterEntity.ChiefInvigilatorRoaster;
import com.app.timetable.Interface.RoasterInterface.ChiefInvigilatorRoasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiefInvigilatorRoasterService {

    @Autowired
    private ChiefInvigilatorRoasterRepository chiefInvigilatorRoasterRepository;

    public List<ChiefInvigilatorRoaster> getAllChiefInvigilatorRoasters() {
        return (List<ChiefInvigilatorRoaster>) chiefInvigilatorRoasterRepository.findAll();
    }

    public ChiefInvigilatorRoaster getChiefInvigilatorRoasterById(Long id) {

        return chiefInvigilatorRoasterRepository.findById(id).orElse(null);
    }

    public ChiefInvigilatorRoaster createChiefInvigilatorRoaster(ChiefInvigilatorRoaster chiefInvigilatorRoaster) {
        return chiefInvigilatorRoasterRepository.save(chiefInvigilatorRoaster);
    }

    public void updateChiefInvigilatorRoaster(Long id, ChiefInvigilatorRoaster chiefInvigilatorRoaster) {
        ChiefInvigilatorRoaster existingChiefInvigilatorRoaster = chiefInvigilatorRoasterRepository.findById(id).orElse(null);
        if (existingChiefInvigilatorRoaster != null) {
            existingChiefInvigilatorRoaster.setChiefInvigilatorID(chiefInvigilatorRoaster.getChiefInvigilatorID());
            existingChiefInvigilatorRoaster.setDate(chiefInvigilatorRoaster.getDate());
            existingChiefInvigilatorRoaster.setTime(chiefInvigilatorRoaster.getTime());
            existingChiefInvigilatorRoaster.setVenue(chiefInvigilatorRoaster.getVenue());
        }
    }

    public void deleteChiefInvigilatorRoaster(Long id) {
        chiefInvigilatorRoasterRepository.deleteById(id);
    }

    public void deleteAll(){
        chiefInvigilatorRoasterRepository.deleteAll();
    }

}


