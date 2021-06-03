package com.app.telehealth.fragments.patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.telehealth.R
import com.app.telehealth.adapters.PatientsAdapter
import com.app.telehealth.models.AppointmentData
import com.app.telehealth.models.PatientProfile
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.fragment_patients.view.*
import java.util.concurrent.CompletableFuture


class PatientsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_patients, container, false)
        view.patientFragmentLoader.isVisible = true
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("patients/")
        val patientList = ArrayList<String>()

        CompletableFuture.runAsync {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            patientList.add(i.value.toString())
                        }
                        patientList.filter{obj: String? -> obj == null}
                        addPatient(patientList)
                    } else {
                        view.patientFragmentLoader.isVisible = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    view.patientFragmentLoader.isVisible = false
                    Log.d("ABCDEF", "this is database error : ${error.message}")
                    Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return view
    }

    private fun addPatient(patientList: ArrayList<String>) {
        val actualPatientData: ArrayList<PatientProfile> = ArrayList()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        for (i in patientList.indices) {
            val databaseReference = firebaseDatabase.getReference("${patientList[i]}/")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
//                        Log.d("ABCDEF", "this is appointment list: ${snapshot.child("appointmentsList").value} ")
                        val appointmentsList = ArrayList<AppointmentData>()
                        if((snapshot.child("appointmentsList").exists())){
                            var list = snapshot.child("appointmentsList").value as ArrayList<*>
                            for(i in list.indices){
                                val map: Map<String, String> = list[i] as Map<String, String>
                                appointmentsList.add(AppointmentData(
                                    patientName = map["patientName"].toString(),
                                    patientUid = map["patientUid"].toString(),
                                    appointmentDate = map["appointmentDate"].toString(),
                                    appointmentTime = map["appointmentTime"].toString(),
                                    instructions = map["instructions"].toString(),
                                    doctorUid = map["doctorUid"].toString(),
                                ))
                            }
                        }
                        val patientProfile = PatientProfile(
                            firstname = snapshot.child("firstname").value.toString(),
                            lastname = snapshot.child("lastname").value.toString(),
                            email = snapshot.child("email").value.toString(),
                            contact = snapshot.child("contact").value.toString(),
                            dob = snapshot.child("dob").value.toString(),
                            gender = snapshot.child("gender").value.toString(),
                            address = snapshot.child("address").value.toString(),
                            city = snapshot.child("city").value.toString(),
                            state = snapshot.child("state").value.toString(),
                            country = snapshot.child("country").value.toString(),
                            patientUid = patientList[i],
                            appointmentsList = appointmentsList
                        )
                        Log.d("ABCDEF", "this is patient profile value: $patientProfile")
                        actualPatientData.add(patientProfile)
                        val patientsAdapter = PatientsAdapter(activity!!, actualPatientData)
                        view?.patientRecyclerView?.layoutManager = LinearLayoutManager(activity!!)
                        patientsAdapter.notifyDataSetChanged()
                        view?.patientRecyclerView?.adapter = patientsAdapter
                        view?.patientFragmentLoader?.isVisible = false
                    } else {
                        view?.patientFragmentLoader?.isVisible = false
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("ABCDEF", "this is database error : ${error.message}")
                    Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}