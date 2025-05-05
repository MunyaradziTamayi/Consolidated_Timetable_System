package com.timetable.timetable.models

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invigilators")
class Timetable {
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var date: String? = null
    var time: String? = null
    var department: String? = null
    var venue: String? = null

    constructor()

    constructor(date: String?, time: String?, department: String?, venue: String?) {
        this.date = date
        this.time = time
        this.department = department
        this.venue = venue
    }
}
