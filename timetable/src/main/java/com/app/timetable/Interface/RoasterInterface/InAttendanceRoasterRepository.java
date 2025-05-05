package com.app.timetable.Interface.RoasterInterface;

import com.app.timetable.Entity.RoasterEntity.InAttendanceRoaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InAttendanceRoasterRepository extends JpaRepository<InAttendanceRoaster, Long> {

}

