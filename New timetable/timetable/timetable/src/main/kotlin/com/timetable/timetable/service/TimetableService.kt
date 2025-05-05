package com.timetable.timetable.service

import com.timetable.timetable.models.Timetable
import com.timetable.timetable.repository.TimetableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TimetableService {
    @Autowired
    private val timetableRepository: TimetableRepository? = null

    val getAll: MutableIterable<Timetable>
        get() = timetableRepository!!.findAll()

    fun getById(id: Long?): Timetable? {
        return timetableRepository!!.findById(id!!).orElse(null)
    }

    fun create(timetable: Timetable): Timetable {
        return timetableRepository!!.save(timetable)
    }

    fun update(id: Long?, timetable: Timetable): Timetable? {
        val existingTimetable: Timetable = timetableRepository!!.findById(id!!).orElse(null)
        existingTimetable.date=timetable.date.toString()
        existingTimetable.time=timetable.time.toString()
        existingTimetable.department=timetable.department
        existingTimetable.venue=timetable.venue
        return timetableRepository.save(existingTimetable)
    }

    fun delete(id: Long?) {
        timetableRepository!!.deleteById(id!!)
    }
}

