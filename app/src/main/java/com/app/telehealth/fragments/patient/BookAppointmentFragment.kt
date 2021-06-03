package com.app.telehealth.fragments.patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.telehealth.R
import com.app.telehealth.adapters.BookAppointmentAdapter
import com.app.telehealth.models.DoctorProfile
import com.app.telehealth.models.PatientProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_book_appointment.view.*
import java.util.*
import kotlin.collections.ArrayList

class BookAppointmentFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view?.bookAppointmenLoader?.isVisible = true
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("doctor/")
        val doctorList = ArrayList<String>()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    doctorList.add(i.value.toString())
                }
                doctorList.filter { obj: String? -> obj == null }
                addDoctor(doctorList)
            }

            override fun onCancelled(error: DatabaseError) {
                view?.bookAppointmenLoader?.isVisible = false
                Log.d("ABCDEF", "this is database error : ${error.message}")
                Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    lateinit var patientProfile: PatientProfile

    //    lateinit var doctorUid: String
    val doctorData: ArrayList<DoctorProfile> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_appointment, container, false)

        patientProfile =
            arguments?.getSerializable("patientDetailsObject") as PatientProfile
/*        if(patientProfile.doctorId != ""){
            doctorUid = patientProfile.doctorId
//            doctorList.contains(doctorUid)
//            doctorList.remove(doctorUid)
        }else{
            doctorUid = ""
        }*/
        view.bookAppointmentSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(submitText: String?): Boolean =
                filterList(doctorData, submitText)

            override fun onQueryTextChange(changingText: String?): Boolean =
                filterList(doctorData, changingText)
        })
        return view
    }

    fun addDoctor(doctorList: ArrayList<String>) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        for (i in doctorList.indices) {
            val databaseReference = firebaseDatabase.getReference("${doctorList[i]}/")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val doctorProfile = DoctorProfile(
                        snapshot.child("firstname").value.toString(),
                        snapshot.child("lastname").value.toString(),
                        snapshot.child("email").value.toString(),
                        snapshot.child("contact").value.toString(),
                        snapshot.child("dob").value.toString(),
                        snapshot.child("gender").value.toString(),
                        snapshot.child("address").value.toString(),
                        snapshot.child("city").value.toString(),
                        snapshot.child("state").value.toString(),
                        snapshot.child("country").value.toString(),
                        snapshot.child("postal").value.toString(),
                        if (snapshot.child("patientList")
                                .exists()
                        ) snapshot.child("patientList").value as ArrayList<String> else ArrayList<String>(),
                        snapshot.child("doctorUid").value.toString()
                    )
                    doctorProfile.patientList.filter { obj: String? -> obj == null }
                    doctorData.add(doctorProfile)
                    val bookAppointmentAdapter =
                        BookAppointmentAdapter(activity!!, doctorData, patientProfile)
                    view?.bookAppointmentRecyclerView?.layoutManager =
                        LinearLayoutManager(activity!!)
                    view?.bookAppointmentRecyclerView?.adapter = bookAppointmentAdapter
                    view?.bookAppointmenLoader?.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    view?.bookAppointmenLoader?.isVisible = false
                    Log.d("ABCDEF", "this is database error : ${error.message}")
                    Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun filterList(doctorData: ArrayList<DoctorProfile>, changingText: String?): Boolean {
        val bookAppointmentAdapter = BookAppointmentAdapter(activity!!, doctorData, patientProfile)
        if (changingText != null && changingText != "") {
            val filteredPatientsData: ArrayList<DoctorProfile> = ArrayList()
            for (i in doctorData.indices) {
                if (("${doctorData[i].firstname.toLowerCase(Locale.ROOT)} ${
                        doctorData[i].lastname.toLowerCase(
                            Locale.ROOT
                        )
                    }").contains(changingText)
                ) {
                    filteredPatientsData.add(doctorData[i])
                }
            }
            val filteredBookAppointmentAdapter =
                BookAppointmentAdapter(activity!!, filteredPatientsData, patientProfile)
            view?.bookAppointmentRecyclerView?.layoutManager = LinearLayoutManager(activity!!)
            view?.bookAppointmentRecyclerView?.adapter = filteredBookAppointmentAdapter
        } else {
            view?.bookAppointmentRecyclerView?.layoutManager = LinearLayoutManager(activity!!)
            view?.bookAppointmentRecyclerView?.adapter = bookAppointmentAdapter
        }
        return false
    }

}