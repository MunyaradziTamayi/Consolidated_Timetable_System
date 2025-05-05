package com.app.timetable.Entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invigilators")
class Invigilator {
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String? = null
    var surname: String? = null
    var email: String? = null
    var department: String? = null

    constructor()
    constructor(name: String?, surname: String?, email: String?,department:String?) {
        this.name = name
        this.surname = surname
        this.email = email
        this.department=department
    }
}
