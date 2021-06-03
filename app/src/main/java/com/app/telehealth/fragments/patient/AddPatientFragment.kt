package com.app.telehealth.fragments.patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_patient.view.*

class AddPatientFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_add_patient, container, false)

//        Log.d("ABCDEF", "patient profile :${patientProfile.address} ")
        view.update.setOnClickListener {
            view.loader.isVisible = true
            val name = view.firstname.text.toString().trim()
            val lastname = view.lastname.text.toString().trim()
            val email = view.email.text.toString().trim()
            val phone = "+91${view.phone.text.toString().trim()}"
            val dob = view.dob.text.toString().trim()
            val gender = "female"
//            val patientProfile = PatientProfile(name, lastname, email, phone, dob, gender)
/*            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference = "patients${getRandomString(20)}"
            val databaseReference = firebaseDatabase.getReference("$reference/")
            databaseReference.setValue(patientProfile)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "patient added", Toast.LENGTH_SHORT).show()
                    addPatient(reference)
                }
                .addOnFailureListener {
                    view.loader.isVisible = false
                    Toast.makeText(activity!!, "patient details failed", Toast.LENGTH_SHORT).show()
                }*/
        }

        return view
    }

    private fun addPatient(patientUid: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val patientList = arrayListOf<String>()
        val databaseReference = firebaseDatabase.getReference("patients/")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    patientList.add(i.value.toString())
                }
                Log.d(Constants.APP_TAG, "this is patient list: $patientList")
                updatePatientsList(patientUid, patientList)
            }

            override fun onCancelled(error: DatabaseError) {
                view?.loader?.isVisible = false
                Toast.makeText(activity!!, "adding patient failed", Toast.LENGTH_SHORT).show()
                Log.d(Constants.APP_TAG, "this is empty doctor list")
            }

        })
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun updatePatientsList(newPatientUid: String, patientList: ArrayList<String>) {
        patientList.add(newPatientUid)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference("patients/")
        reference.setValue(patientList).addOnSuccessListener {
            view?.loader?.isVisible = false
            Toast.makeText(activity!!, "patient added successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                view?.loader?.isVisible = false
                Toast.makeText(activity!!, "patient details failed", Toast.LENGTH_SHORT).show()
            }

    }

}