package com.app.telehealth.fragments.patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.telehealth.R
import kotlinx.android.synthetic.main.fragment_patient_details.view.*
import java.text.SimpleDateFormat
import java.util.*


class PatientDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patient_details, container, false)
/*        val patientProfile: PatientProfile = arguments?.getSerializable("patientDetailsObject") as PatientProfile
        val completeDOB = patientProfile.dob.split("-")
        val age = completeDOB[2].toInt() - Calendar.getInstance().weekYear
        val originalFormat = SimpleDateFormat("yyyy-MM-dd")
        val targetDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val date = originalFormat.parse(patientProfile.dob)
        val formattedDate = targetDateFormat.format(date)
        //name age dob address gender email contact
        view.patientDetailsName.append(" ${patientProfile.firstname} ${patientProfile.lastname}")
        view.patientDetailsAge.append(" $age")
        view.patientDetailsDOB.append(" $formattedDate")
        view.patientDetailsAddress.append(" ${patientProfile.address}, ${patientProfile.city}, ${patientProfile.state}, ${patientProfile.country}")
        view.patientDetailsGender.append(" ${patientProfile.gender}")
        view.patientDetailsEmail.append(" ${patientProfile.email}")
        view.patientDetailsContact.append(" ${patientProfile.contact}")*/







        return view
    }


}