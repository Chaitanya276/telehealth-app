package com.app.telehealth.models

import java.io.Serializable

class DoctorProfile : Serializable {
    val firstname: String
    val lastname: String
    val email: String
    val contact: String
    var dob: String
    var gender: String
    var address: String
    var city: String
    var state: String
    var country: String
    private var postal: String
    var patientList: ArrayList<String> = ArrayList()
    var doctorUid: String
    constructor(
        firstname: String, lastname: String, email: String, contact: String,
        dob: String? = "",
        gender: String? = "",
        address: String? = "",
        city: String? = "",
        state: String? = "",
        country: String? = "",
        postal: String? = "",
        patientList: ArrayList<String>,
        doctorUid: String
    ) {
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.contact = contact
        this.dob = dob.toString()
        this.gender = gender.toString()
        this.address = address.toString()
        this.city = city.toString()
        this.state = state.toString()
        this.country = country.toString()
        this.postal = postal.toString()
        this.patientList = patientList
        this.doctorUid = doctorUid
    }
}