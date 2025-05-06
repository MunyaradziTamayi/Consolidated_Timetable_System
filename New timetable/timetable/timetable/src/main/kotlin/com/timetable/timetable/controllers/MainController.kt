package com.timetable.timetable.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Controller
@RequestMapping("/")
class MainController {

        @GetMapping("/")
        fun home(): String {
            println("home called")
            return "index"
        }

}
