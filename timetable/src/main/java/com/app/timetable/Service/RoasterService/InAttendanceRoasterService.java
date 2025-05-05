package com.app.timetable.Service.RoasterService;

import com.app.timetable.Entity.RoasterEntity.InAttendanceRoaster;
import com.app.timetable.Interface.RoasterInterface.InAttendanceRoasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InAttendanceRoasterService {

    @Autowired
    private InAttendanceRoasterRepository inAttendanceRoasterRepository;

    public List<InAttendanceRoaster> getAllInAttendanceRoasters() {
        return (List<InAttendanceRoaster>) inAttendanceRoasterRepository.findAll();
    }

    public InAttendanceRoaster getInAttendanceRoasterById(Long id) {

        return inAttendanceRoasterRepository.findById(id).orElse(null);
    }

    public InAttendanceRoaster createInAttendanceRoaster(InAttendanceRoaster inAttendanceRoaster) {
        return inAttendanceRoasterRepository.save(inAttendanceRoaster);
    }

    public InAttendanceRoaster updateInAttendanceRoaster(Long id, InAttendanceRoaster inAttendanceRoaster) {
        InAttendanceRoaster existingInAttendanceRoaster = inAttendanceRoasterRepository.findById(id).orElse(null);
        if (existingInAttendanceRoaster != null) {
            existingInAttendanceRoaster.setInAttendanceID(inAttendanceRoaster.getInAttendanceID());
            existingInAttendanceRoaster.setDate(inAttendanceRoaster.getDate());
            existingInAttendanceRoaster.setTime(inAttendanceRoaster.getTime());
            existingInAttendanceRoaster.setVenue(inAttendanceRoaster.getVenue());
            return inAttendanceRoasterRepository.save(existingInAttendanceRoaster);
        } else {
            return null;
        }
    }

    public void deleteInAttendanceRoaster(Long id) {
        inAttendanceRoasterRepository.deleteById(id);
    }

    public void deleteAll(){
        inAttendanceRoasterRepository.deleteAll();
    }


}

