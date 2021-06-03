package com.app.telehealth.fragments.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.telehealth.Constants
import com.app.telehealth.PatientActivity
import com.app.telehealth.R
import com.app.telehealth.adapters.AppointmentAdapter
import com.app.telehealth.models.AppointmentData
import com.app.telehealth.models.DoctorProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_patient.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {
    lateinit var doctorProfile: DoctorProfile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        view.patientInfo.setOnClickListener {
            val intent = Intent(activity, PatientActivity::class.java)
            startActivity(intent)
        }
        val uid = FirebaseAuth.getInstance().uid
        fetchPatientList(uid.toString())
        view.dashboardPullToReferesh.setOnRefreshListener {
            view.dashboardNoAppointments.isVisible = false
            fetchPatientList(uid.toString())
        }

        return view
    }

/*    private fun calculateLength(patientlist: ArrayList<String>){
        var listt = ArrayList<String>()
        for(i in patientlist.indices){
            listt.add(patientlist[i])
        }
        Log.d("ABCDEF", "list length: ${listt.size}")
        listt.removeIf{obj: String? -> obj == null}
        Log.d("ABCDEF", "list : $listt")
    }*/

    private fun fetchPatientList(doctorUid: String) {
        view?.dashboardLoader?.isVisible = true
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference("$doctorUid/")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                doctorProfile = DoctorProfile(
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
                    if(snapshot.child("patientList").exists())
                    snapshot.child("patientList").value as ArrayList<String> else ArrayList<String>(),
                    snapshot.child("doctorUid").value.toString()
                )
                val patientList = doctorProfile.patientList
                var updatedPatientList = ArrayList<String>()
                if(patientList.size > 1){
                    updatedPatientList = patientList.distinct().toList() as ArrayList<String>
                }
                view?.dashboardRecyclerView?.isVisible = true
                if (updatedPatientList.size == 0 || (updatedPatientList.size == 1  && updatedPatientList[0] == "")) {
                    noAppointments()
                } else {
                    patientList.filter { obj: String? -> obj == null }
                    fetchPatientDetails(updatedPatientList, doctorUid)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                view?.dashboardLoader?.isVisible = false
                Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchPatientDetails(patientList: ArrayList<String>, doctorUid: String) {
        view?.dashboardPullToReferesh?.isRefreshing = false
        view?.dashboardLoader?.isVisible = true
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val appointmentDataList: ArrayList<AppointmentData> = ArrayList()
        for (i in patientList.indices) {
            if (patientList[i] != "" && !patientList[i].isNullOrBlank()) {
                val reference = firebaseDatabase.getReference("${patientList[i]}/appointmentsList")
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
//                            if((snapshot.child("appointmentsList").exists())){
                                var list = snapshot.value as ArrayList<*>
                                for(i in list.indices){
                                    val map: Map<String, String> = list[i] as Map<String, String>
                                    if(map["doctorUid"].equals(doctorUid)){
                                        appointmentDataList.add(AppointmentData(
                                            patientName = map["patientName"].toString(),
                                            patientUid = map["patientUid"].toString(),
                                            appointmentDate = map["appointmentDate"].toString(),
                                            appointmentTime = map["appointmentTime"].toString(),
                                            instructions = map["instructions"].toString(),
                                            doctorUid = map["doctorUid"].toString(),
                                        ))
                                    }
                                }
                            val appointmentAdapter = AppointmentAdapter(
                                activity!!,
                                appointmentDataList,
                                "confirmedAppointment",
                                doctorProfile
                            )
                            view?.dashboardRecyclerView?.layoutManager = LinearLayoutManager(
                                activity!!
                            )
                            view?.dashboardRecyclerView?.adapter = appointmentAdapter
                            view?.dashboardLoader?.isVisible = false
                            view?.dashboardPullToReferesh?.isRefreshing = false
                        } else
                            view?.dashboardLoader?.isVisible = false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        view?.dashboardLoader?.isVisible = false
                        Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun noAppointments() {
        view?.dashboardNoAppointments?.isVisible = true
        view?.dashboardNoAppointments?.textSize = 20.toFloat()
        view?.dashboardNoAppointments?.gravity = Gravity.CENTER_VERTICAL
        view?.dashboardRecyclerView?.isVisible = false
        view?.dashboardLoader?.isVisible = false
        view?.dashboardPullToReferesh?.isRefreshing = false
    }

    private fun setUpCards(appointmentDataList: ArrayList<AppointmentData>) {
        Constants.setUpRecycler(
            view!!.dashboardRecyclerView,
            LinearLayoutManager(activity),
            activity!!,
            appointmentDataList,
            "confirmedAppointment",
            doctorProfile
        )
        view?.dashboardPullToReferesh?.isRefreshing = false
        view?.dashboardLoader?.isVisible = false
    }


}