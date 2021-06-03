package com.app.telehealth.models

import java.io.Serializable

class PatientProfile : Serializable {
    var firstname: String
    var lastname: String
    var email: String
    var contact: String
    var dob: String
    var gender: String
    var address: String
    var city: String
    var state: String
    var country: String
    var appointmentsList: ArrayList<AppointmentData> = ArrayList()

    constructor(
        firstname: String,
        lastname: String,
        email: String,
        contact: String,
        dob: String,
        gender: String,
        address: String,
        city: String,
        state: String,
        country: String,
        appointmentsList: ArrayList<AppointmentData>,
        patientUid: String
    ) {
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.contact = contact
        this.dob = dob
        this.gender = gender
        this.address = address
        this.city = city
        this.state = state
        this.country = country
        this.appointmentsList = appointmentsList
        this.patientUid = patientUid
    }

    var patientUid: String

}