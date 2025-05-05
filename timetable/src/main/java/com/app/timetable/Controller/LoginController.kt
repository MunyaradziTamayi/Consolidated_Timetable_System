package com.app.timetable.Controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam





@Controller
@RequestMapping("/")
class LoginController {

    @GetMapping("/login")
    fun loginPage():String{
        return "login"
    }

    @PostMapping("/")
    fun handleLogin(
            @RequestParam("email") email: String,
            @RequestParam("password") password: String,
            model: Model): String {

        //decide where to send the user based on the email address
        return "home"

    }

}