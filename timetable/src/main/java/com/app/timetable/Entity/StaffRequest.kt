package com.app.timetable.Entity

data class StaffRequest(
    var date:String,
    var time:String,
    var venue:String,
    var type:String
){
    companion object{
        const val ADMINISTRATOR="administrator"
        const val CHIEF_INVIGILATOR="chief invigilator"
        const val SUPPORTING_STAFF="supporting staff"
    }
}
