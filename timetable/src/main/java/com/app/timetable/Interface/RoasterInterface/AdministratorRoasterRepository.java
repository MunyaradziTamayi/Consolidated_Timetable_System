package com.app.timetable.Interface.RoasterInterface;

import com.app.timetable.Entity.RoasterEntity.AdministratorRoaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRoasterRepository extends JpaRepository<AdministratorRoaster, Long> {

}

