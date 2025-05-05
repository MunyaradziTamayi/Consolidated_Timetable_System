package com.app.timetable.Service.RoasterService;

import com.app.timetable.Entity.RoasterEntity.AssistantInvigilatorRoaster;
import com.app.timetable.Entity.RoasterEntity.ChiefInvigilatorRoaster;
import com.app.timetable.Interface.RoasterInterface.AssistantInvigilatorRoasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssistantInvigilatorRoasterService {

    @Autowired
    private AssistantInvigilatorRoasterRepository assistantInvigilatorRoasterRepository;

    public List<AssistantInvigilatorRoaster> getAllAssistantInvigilatorRoasters() {
        return (List<AssistantInvigilatorRoaster>) assistantInvigilatorRoasterRepository.findAll();
    }

    public AssistantInvigilatorRoaster getAssistantInvigilatorRoasterById(Long id) {

        return assistantInvigilatorRoasterRepository.findById(id).orElse(null);
    }

    public AssistantInvigilatorRoaster createAssistantInvigilatorRoaster(AssistantInvigilatorRoaster assistantInvigilatorRoaster) {
        return assistantInvigilatorRoasterRepository.save(assistantInvigilatorRoaster);
    }

    public void updateAssistantInvigilatorRoaster(Long id, AssistantInvigilatorRoaster assistantInvigilatorRoaster) {
        AssistantInvigilatorRoaster existingAssistantInvigilatorRoaster = assistantInvigilatorRoasterRepository.findById(id).orElse(null);
        if (existingAssistantInvigilatorRoaster != null) {
            existingAssistantInvigilatorRoaster.setInvigilatorID(assistantInvigilatorRoaster.getInvigilatorID());
            existingAssistantInvigilatorRoaster.setDate(assistantInvigilatorRoaster.getDate());
            existingAssistantInvigilatorRoaster.setTime(assistantInvigilatorRoaster.getTime());
            existingAssistantInvigilatorRoaster.setVenue(assistantInvigilatorRoaster.getVenue());
        }

        assistantInvigilatorRoasterRepository.save(assistantInvigilatorRoaster);
    }

    public void deleteAssistantInvigilatorRoaster(Long id) {
        assistantInvigilatorRoasterRepository.deleteById(id);
    }

    public void deleteAll(){
        assistantInvigilatorRoasterRepository.deleteAll();
    }

}


