package com.app.timetable.Service.RoasterService;

import com.app.timetable.Entity.RoasterEntity.InvigilatorRoaster;
import com.app.timetable.Interface.RoasterInterface.InvigilatorRoasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvigilatorRoasterService {

    @Autowired
    private InvigilatorRoasterRepository invigilatorRoasterRepository;

    public List<InvigilatorRoaster> getAllInvigilatorRoasters() {
        return (List<InvigilatorRoaster>) invigilatorRoasterRepository.findAll();
    }

    public InvigilatorRoaster getInvigilatorRoasterById(Long id) {

        return invigilatorRoasterRepository.findById(id).orElse(null);
    }

    public InvigilatorRoaster createInvigilatorRoaster(InvigilatorRoaster invigilatorRoaster) {
        return invigilatorRoasterRepository.save(invigilatorRoaster);
    }

    public InvigilatorRoaster updateInvigilatorRoaster(Long id, InvigilatorRoaster invigilatorRoaster) {
        InvigilatorRoaster existingInvigilatorRoaster = invigilatorRoasterRepository.findById(id).orElse(null);
        if (existingInvigilatorRoaster != null) {
            existingInvigilatorRoaster.setExam(invigilatorRoaster.getExam());
            existingInvigilatorRoaster.setInvigilatorID(invigilatorRoaster.getInvigilatorID());
            return invigilatorRoasterRepository.save(existingInvigilatorRoaster);
        } else {
            return null;
        }
    }

    public void deleteInvigilatorRoaster(Long id) {
        invigilatorRoasterRepository.deleteById(id);
    }

    public void deleteAll(){
        invigilatorRoasterRepository.deleteAll();
    }

}

