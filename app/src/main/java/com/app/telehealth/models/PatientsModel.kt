package com.app.telehealth.models

class PatientsModel {

    val  patientsName: String
    val patientsAge: String
    var patientsDOB: String = ""
    val patientsGender: String
    var patientsAddress: String = ""
    var patientsEmail: String = ""
    var patientsContactNo: String = ""

    constructor(
        patientsName: String,
        patientsAge: String,
        patientsDOB: String,
        patientsGender: String,
        patientsAddress: String,
        patientsEmail: String,
        patientsContactNo: String
    ) {
        this.patientsName = patientsName
        this.patientsAge = patientsAge
        this.patientsDOB = patientsDOB
        this.patientsGender = patientsGender
        this.patientsAddress = patientsAddress
        this.patientsEmail = patientsEmail
        this.patientsContactNo = patientsContactNo
    }

    constructor(patientsName: String,
                patientsAge: String,patientsGender: String,){
        this.patientsName = patientsName
        this.patientsAge = patientsAge
        this.patientsGender = patientsGender
    }


}