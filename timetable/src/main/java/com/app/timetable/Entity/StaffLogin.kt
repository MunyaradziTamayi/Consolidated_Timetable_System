package com.app.timetable.Entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "login")
class StaffLogin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var email:String?=null
    var password:String?=null
    var access:String?=null
    constructor()

    constructor(email:String, password:String,access:String){
        this.email=email
        this.password=password
        this.access=access
    }
    companion object StaffType{
        var EXAM_STAFF="exam staff"
        var HOD="head of department"
    }
}

