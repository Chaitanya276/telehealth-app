package com.app.telehealth.models

class AppointmentData {

    val  patientName: String
    var appointmentDate: String
    val appointmentTime: String
    val patientUid: String
    val doctorUid: String
    val instructions: String

    constructor(
        patientName: String,
        appointmentDate: String,
        appointmentTime: String,
        patientUid: String,
        doctorUid: String,
        instructions: String
    ) {
        this.patientName = patientName
        this.appointmentDate = appointmentDate
        this.appointmentTime = appointmentTime
        this.patientUid = patientUid
        this.doctorUid = doctorUid
        this.instructions = instructions
    }
}