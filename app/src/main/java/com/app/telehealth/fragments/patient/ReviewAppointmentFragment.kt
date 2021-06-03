package com.app.telehealth.fragments.patient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.HomeActivity
import com.app.telehealth.R
import com.app.telehealth.models.AppointmentData
import com.app.telehealth.models.DoctorProfile
import com.app.telehealth.models.PatientProfile
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_appointment_review.view.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class ReviewAppointmentFragment : Fragment() {


    lateinit var doctorProfile: DoctorProfile
    lateinit var patientProfileUpdated: PatientProfile
    lateinit var appointmentTime: String
    lateinit var appointmentDate: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_appointment_review, container, false)

        doctorProfile = arguments?.getSerializable("doctorProfileObject") as DoctorProfile
        patientProfileUpdated = arguments?.getSerializable("patientProfileObject") as PatientProfile
        appointmentTime = arguments?.getString("appointmentTime").toString()
        appointmentDate = arguments?.getString("appointmentDate").toString()
        val name: String =
            doctorProfile.firstname.capitalize(Locale.ROOT) + " " + doctorProfile.lastname.capitalize(
                Locale.ROOT
            )

        val age: String =
            (if (!doctorProfile.dob.isNullOrBlank() && doctorProfile.dob != "" && doctorProfile.dob.isNotBlank()) {
                Constants.getAge(doctorProfile.dob, "-")
            } else "")
//        val patientUid = activity!!.getSharedPreferences("SHARED", Context.MODE_PRIVATE)
//            .getString("patientUid", null)
        val gender: String? = arguments?.getString("gender")
        view.reviewAppointmentName.text = name
        view.reviewAppointmentAge.text = age
        view.reviewAppointmentGender.text = gender
        view.reviewAppointmentDate.append(" $appointmentDate")
        view.reviewAppointmentTime.append(" $appointmentTime")
        view.reviewAppointmentConfirm.setOnClickListener {
            view.reviewAppointmentLoader.isVisible = true
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                //doInBackground()...
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val reference = firebaseDatabase.getReference("${doctorProfile.doctorUid}/")
                doctorProfile.patientList.add(patientProfileUpdated.patientUid)
                val updatedPatientList: ArrayList<String> = doctorProfile.patientList.distinct() as ArrayList<String>
                doctorProfile.patientList = updatedPatientList
                reference.setValue(doctorProfile)
                    .addOnSuccessListener {
                        val appointmentData = AppointmentData(
                            patientName = "${patientProfileUpdated.firstname} ${patientProfileUpdated.lastname}",
                            appointmentDate = appointmentDate,
                            appointmentTime = appointmentTime,
                            patientUid = patientProfileUpdated.patientUid,
                            doctorUid = doctorProfile.doctorUid,
                            instructions = if (view?.reviewInstructions?.text.toString().trim()
                                    .equals("")
                            ) " " else view?.reviewInstructions?.text.toString().trim()
                        )
//                        val appointmentList = ArrayList<AppointmentData>()
                        patientProfileUpdated.appointmentsList.add(appointmentData)
//                        patientProfileUpdated.appointmentsList = appointmentList
                        updatePatient(patientProfileUpdated)
                    }
                    .addOnFailureListener {
                        Log.d("ABCDEF", "this is exception: ${it.message}")
                        view?.reviewAppointmentLoader?.isVisible = false
                        Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
        return view
    }

    private fun updatePatient(patientProfile: PatientProfile) {
        val patientUid: String = patientProfile.patientUid
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference("$patientUid/")
        reference.setValue(patientProfile)
            .addOnSuccessListener {
                view?.reviewAppointmentLoader?.isVisible = false
                Toast.makeText(activity!!, "Appointment confirmed.", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity!!, HomeActivity()::class.java)
                intent.putExtra("setAppointmentTab", true)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("ABCDEF", "this is exception: ${it.message}")
                view?.reviewAppointmentLoader?.isVisible = false
                Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
            }
    }

}