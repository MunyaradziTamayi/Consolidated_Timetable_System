package com.timetable.timetable.repository


import com.timetable.timetable.models.Timetable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository;

@Repository
interface TimetableRepository:CrudRepository<Timetable,Long>{}