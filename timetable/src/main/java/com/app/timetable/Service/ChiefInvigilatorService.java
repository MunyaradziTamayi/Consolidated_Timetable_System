package com.app.timetable.Service;

import com.app.timetable.Entity.Administrator;
import com.app.timetable.Entity.ChiefInvigilator;
import com.app.timetable.Interface.ChiefInvigilatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiefInvigilatorService {

    @Autowired
    private ChiefInvigilatorRepository chiefInvigilatorRepository;

    public List<ChiefInvigilator> getAllChiefInvigilators() {
        return (List<ChiefInvigilator>) chiefInvigilatorRepository.findAll();
    }

    public ChiefInvigilator getChiefInvigilatorById(Long id) {

        return chiefInvigilatorRepository.findById(id).orElse(null);
    }

    public ChiefInvigilator createChiefInvigilator(ChiefInvigilator chiefInvigilator) {
        return chiefInvigilatorRepository.save(chiefInvigilator);
    }

    public ChiefInvigilator updateChiefInvigilator(Long id, ChiefInvigilator chiefInvigilator) {
        ChiefInvigilator existingChiefInvigilator = chiefInvigilatorRepository.findById(id).orElse(null);
        if (existingChiefInvigilator != null) {
            existingChiefInvigilator.setName(chiefInvigilator.getName());
            existingChiefInvigilator.setSurname(chiefInvigilator.getSurname());
            existingChiefInvigilator.setEmail(chiefInvigilator.getEmail());
            return chiefInvigilatorRepository.save(existingChiefInvigilator);
        } else {
            return null;
        }
    }

    public void deleteChiefInvigilator(Long id) {
        chiefInvigilatorRepository.deleteById(id);
    }
}

