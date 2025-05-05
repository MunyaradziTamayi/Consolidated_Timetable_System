package com.timetable.timetable.service

import com.timetable.timetable.helpers.PDFTableStripper
import com.timetable.timetable.models.Timetable
import org.apache.pdfbox.Loader
import java.io.File
import java.io.IOException


object FileService {

    private val forbidden= arrayOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday",
        "Lunch",
        "Break"
    )

    @Throws(IOException::class)
    fun getExamListFromPDF(file: File): ArrayList<Timetable> {
        val timetables: ArrayList<Timetable> = ArrayList()

        val document = Loader.loadPDF(file)

        val stripper = PDFTableStripper()
        stripper.sortByPosition = true


        for (page in 0..<document.numberOfPages) {
            val pdPage = document.getPage(page)

            stripper.extractTable(pdPage)

            var time=""

            for (r in 0..<stripper.rows) {
                if (stripper.getText(r,0).trim()!=""){
                    time=stripper.getText(r,0).trim()
                }
                if(time==""){
                    continue
                }
                for (c in 1..<stripper.columns) {
                    val timetable = Timetable(
                        getDate(c),
                        time,
                        getDepartment(stripper.getText(r,c)),
                        getVenue(stripper.getText(r,c)),
                    )
                    if (timetable.department!=null&&timetable.venue!=null) {
                        timetables.add(timetable)
                    }
                }


            }
        }

        return timetables
    }

    private fun getDate(column:Int):String? {

        return when (column) {
            1-> "Monday"
            2-> "Tuesday"
            3-> "Wednesday"
            4-> "Thursday"
            5-> "Friday"
            else-> null
        }
    }

    private fun getDepartment(text: String):String? {
        return if(text.contains("-")) {
            text.split("-")[0].trim()
        }else{
            null
        }
    }

    private fun getVenue(text:String):String? {
        return if(text.contains("-")) {
            text.split("-")[1].trim()
        }else{
            null
        }
    }
}
