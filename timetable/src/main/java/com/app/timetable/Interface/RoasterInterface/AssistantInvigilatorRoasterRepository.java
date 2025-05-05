package com.app.timetable.Interface.RoasterInterface;

import com.app.timetable.Entity.RoasterEntity.AssistantInvigilatorRoaster;
import com.app.timetable.Entity.RoasterEntity.InvigilatorRoaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantInvigilatorRoasterRepository extends JpaRepository<AssistantInvigilatorRoaster, Long> {

}


