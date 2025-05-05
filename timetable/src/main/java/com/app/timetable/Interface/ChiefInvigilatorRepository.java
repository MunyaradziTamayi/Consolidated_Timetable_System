package com.app.timetable.Interface;

import com.app.timetable.Entity.ChiefInvigilator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiefInvigilatorRepository extends JpaRepository<ChiefInvigilator, Long> {

}

