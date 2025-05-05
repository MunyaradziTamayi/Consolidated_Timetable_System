package com.app.timetable.Controller

import com.app.timetable.Entity.StaffEntity
import com.app.timetable.Entity.StaffRequest
import com.app.timetable.Service.AdministratorService
import com.app.timetable.Service.ChiefInvigilatorService
import com.app.timetable.Service.InAttendanceService
import com.app.timetable.Service.InvigilatorService
import com.app.timetable.Service.RoasterService.AdministratorRoasterService
import com.app.timetable.Service.RoasterService.ChiefInvigilatorRoasterService
import com.app.timetable.Service.RoasterService.InAttendanceRoasterService
import com.app.timetable.Service.RoasterService.InvigilatorRoasterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class RestController {

    @Autowired
    lateinit var invigilatorService: InvigilatorService

    @Autowired
    lateinit var invigilatorRoasterService: InvigilatorRoasterService

    @Autowired
    lateinit var administratorService: AdministratorService

    @Autowired
    lateinit var administratorRoasterService:AdministratorRoasterService

    @Autowired
    lateinit var chiefInvigilatorService: ChiefInvigilatorService

    @Autowired
    lateinit var chiefInvigilatorRoasterService: ChiefInvigilatorRoasterService

    @Autowired
    lateinit var inAttendanceService: InAttendanceService

    @Autowired
    lateinit var inAttendanceRoasterService: InAttendanceRoasterService



    @PostMapping("/invigilator_list")
    fun getInvigilators(@RequestBody exam:String):ResponseEntity<List<StaffEntity>>{

        val list= ArrayList<StaffEntity>()

        val invRoaster=invigilatorRoasterService.allInvigilatorRoasters.filter {
            it.exam==exam.toLong()
        }
        var count=1

        invigilatorService.allInvigilators.forEach {inv->
            val assigned= invRoaster.any { it.invigilatorID==inv.id}

            val staffEntity=StaffEntity(
                count.toString(),
                inv.name,
                inv.surname,
                inv.email,
                assigned
            )

            list.add(staffEntity)
            count+=1
        }

        return ResponseEntity(list, HttpStatus.OK)

    }

    @PostMapping("/staff_list")
    fun getStaff(@RequestBody staffRequest: StaffRequest):ResponseEntity<List<StaffEntity>>{

        val list= ArrayList<StaffEntity>()

        when(staffRequest.type){
            StaffRequest.ADMINISTRATOR->{
                val roaster=administratorRoasterService.allAdministratorRoasters.filter {
                    it.date==staffRequest.date
                            &&it.time==staffRequest.time
                            &&it.venue==staffRequest.venue
                }
                var count=1

                administratorService.allAdministrators.forEach {staff->
                    val assigned= roaster.any { it.administratorID==staff.id}

                    val staffEntity=StaffEntity(
                        count.toString(),
                        staff.name,
                        staff.surname,
                        staff.email,
                        assigned
                    )

                    list.add(staffEntity)
                    count+=1
                }
            }
            StaffRequest.SUPPORTING_STAFF->{
                val roaster=inAttendanceRoasterService.allInAttendanceRoasters.filter {
                    it.date==staffRequest.date
                            &&it.time==staffRequest.time
                            &&it.venue==staffRequest.venue
                }
                var count=1

                inAttendanceService.allInAttendance.forEach {staff->
                    val assigned= roaster.any { it.inAttendanceID==staff.id}

                    val staffEntity=StaffEntity(
                        count.toString(),
                        staff.name,
                        staff.surname,
                        staff.email,
                        assigned
                    )

                    list.add(staffEntity)
                    count+=1
                }
            }
            StaffRequest.CHIEF_INVIGILATOR->{
                val roaster=chiefInvigilatorRoasterService.allChiefInvigilatorRoasters.filter {
                    it.date==staffRequest.date
                            &&it.time==staffRequest.time
                            &&it.venue==staffRequest.venue
                }
                var count=1

                chiefInvigilatorService.allChiefInvigilators.forEach {staff->
                    val assigned= roaster.any { it.chiefInvigilatorID==staff.id}

                    val staffEntity=StaffEntity(
                        count.toString(),
                        staff.name,
                        staff.surname,
                        staff.email,
                        assigned
                    )

                    list.add(staffEntity)
                    count+=1
                }
            }
        }

        return ResponseEntity(list, HttpStatus.OK)

    }




}