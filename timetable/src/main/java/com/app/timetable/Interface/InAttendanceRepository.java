package com.app.timetable.Interface;

import com.app.timetable.Entity.Administrator;
import com.app.timetable.Entity.InAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InAttendanceRepository extends JpaRepository<InAttendance, Long> {

}

