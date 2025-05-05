package com.timetable.timetable.controllers

import com.timetable.timetable.helpers.MultiPartFileToFileConverter
import com.timetable.timetable.models.DateTime
import com.timetable.timetable.models.TableModel
import com.timetable.timetable.models.Timetable
import com.timetable.timetable.service.FileService
import com.timetable.timetable.service.TimetableService
import org.apache.poi.hssf.usermodel.HeaderFooter.file
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@RestController
@RequestMapping("/")
class RestController {

    @Autowired
    private lateinit var timetableService: TimetableService

    @PostMapping("/upload")
    @Throws(IOException::class)
    fun mapReapExcelDataToDB(@RequestParam("file") multipartFile: MultipartFile): ResponseEntity<Void> {

        saveToDatabase(multipartFile)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/free_slots")
    fun freeSlots(@RequestBody dateTime: DateTime): ResponseEntity<List<TableModel>> {

        val list= ArrayList<TableModel>()
        val tList=timetableService.getAll
        val usedVenues=ArrayList<String>()
        val allVenues=getAllVenues()
        var count=1

        tList.forEach {
            if (it.date==dateTime.date&&it.time==dateTime.time) {
                usedVenues.add(it.venue!!)
            }
        }

        allVenues.forEach {
            if (!usedVenues.contains(it)){
                list.add(TableModel(count.toString(),it))
                count++
            }
        }

        return ResponseEntity(list,HttpStatus.OK)
    }

    private fun saveToDatabase(multipartFile: MultipartFile) {
        val data: ArrayList<Timetable> = FileService.getExamListFromPDF(MultiPartFileToFileConverter.convert(multipartFile))
        for (timetable in data) {
            timetableService.create(timetable)
        }
    }

    private fun getAllVenues():ArrayList<String>{
        val list=timetableService.getAll
        val venues=ArrayList<String>()

        list.forEach{
            if(!venues.contains(it.venue)){
                venues.add(it.venue!!)
            }
        }

        return venues
    }



}