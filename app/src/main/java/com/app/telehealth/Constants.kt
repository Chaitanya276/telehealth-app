package com.app.telehealth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.telehealth.adapters.AppointmentAdapter
import com.app.telehealth.fragments.home.ConfirmedAppointmentsFragment
import com.app.telehealth.fragments.home.DashboardFragment
import com.app.telehealth.models.AppointmentData
import com.app.telehealth.models.DoctorProfile
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_appointments_confirmed.*
import kotlinx.android.synthetic.main.fragment_appointments_confirmed.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {
        const val SHARED_TAG = "telehealthapp"
        const val APP_TAG = "APPTELEHEALTH"
        var serializedCode = true
        const val TIME_DELAY: Long = 5000
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        val passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+=])" +  //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{4,}" +  //at least 4 characters
                "$"
        fun formValidation(
            firstnameET: EditText? = null, firstnameLayout: TextInputLayout? = null,
            lastnameET: EditText? = null, lastnameLayout: TextInputLayout? = null,
            phoneET: EditText? = null, phoneLayout: TextInputLayout? = null,
            emailET: EditText? = null, emailLayout: TextInputLayout? = null,
            passwordET: EditText? = null, passwordLayout: TextInputLayout? = null,
            confrimaPasswordET: EditText? = null, confrimaPasswordLayout: TextInputLayout? = null,
        ): Boolean {
            var validated: Boolean = true
            val passwordText = passwordET?.text.toString().trim()
            if (confrimaPasswordET != null && confrimaPasswordLayout != null) {
                val confirmPasswordText = confrimaPasswordET.text.toString().trim()
                if (confirmPasswordText.isEmpty()) {
                    confrimaPasswordET.requestFocus()
                    confrimaPasswordLayout.error = "Confirm password required"
                    validated = false
                } else if (!passwordText.equals(confirmPasswordText)) {
                    confrimaPasswordET.requestFocus()
                    confrimaPasswordLayout.error = "Check confirm password"
                    validated = false
                } else {
                    confrimaPasswordLayout.error = null
                }
            }
            if (passwordET != null && passwordLayout != null) {
                if (passwordText.isEmpty()) {
                    passwordET.requestFocus()
                    passwordLayout.error = "Password required"
                    validated = false
                }else if(passwordText.length < 6){
                    passwordET.requestFocus()
                    passwordLayout.error = "Atleast 6 characters"
                    validated = false
                }
                else if (!passwordText.matches(passwordVal.toRegex())){
                    passwordET.requestFocus()
                    passwordLayout.error = "Password too weak"
                    validated = false
                }else {
                    passwordLayout.error = null
                }
            }
            if (phoneET != null && phoneLayout != null) {
                val phoneText = phoneET.text.toString().trim()
                if (phoneText.isEmpty()) {
                    phoneET.requestFocus()
                    phoneLayout.error = "Phone no. required"
                    validated = false
                } else if (phoneText.length != 10) {
                    phoneET.requestFocus()
                    phoneLayout.error = "Enter valid phone no."
                    validated = false
                } else {
                    phoneLayout.error = null
                }
            }
            if (emailET != null && emailLayout != null) {
                val emailText = emailET.text.toString().trim()
                if (emailText.isEmpty()) {
                    val loginEmail = emailET
                    emailET.requestFocus()
                    emailLayout.error = "Email required"
                    validated = false
                } else if (!emailText.matches(emailPattern)) {
                    emailET.requestFocus()
                    emailLayout.error = "Please enter valid email"
                    validated = false
                } else {
                    emailLayout.error = null
                }
            }
            if (lastnameET != null && lastnameLayout != null) {
                val lastnameText = lastnameET.text.toString().trim()
                if (lastnameText.isEmpty()) {
                    lastnameET.requestFocus()
                    lastnameLayout.error = "Lastname required"
                    validated = false
                } else {
                    lastnameLayout.error = null
                }
            }
            if (firstnameET != null && firstnameLayout != null) {
                val firstnameText = firstnameET.text.toString().trim()
                if (firstnameText.isEmpty()) {
                    firstnameET.requestFocus()
                    firstnameLayout.error = "Firstname required"
                    validated = false
                } else {
                    firstnameLayout.error = null
                }
            }
            return validated
        }

        fun navigateTo(
            context: Activity,
            navigateTo: Activity,
            clearBackActivities: Boolean? = false,
            putExtra: String? = null
        ) {
            val intent = Intent(context, navigateTo::class.java)
            if (clearBackActivities == true) {
                intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            }
            if (putExtra != null) {
                intent.putExtra("buttonName", putExtra)
            }
            context.startActivity(intent)
            if (clearBackActivities == true) {
                context.finish()
            }
        }

        fun navigateFragment(
            nextFragment: Fragment,
            currentFragmentContext: FragmentActivity,
            frame: Int,
            addBackStack: Boolean? = false
        ) {
            val supportFragment = currentFragmentContext.supportFragmentManager
            val manager = supportFragment.beginTransaction()
            if (addBackStack == false) {
                manager.replace(frame, nextFragment)
                    .commit()
            } else {
                manager.replace(frame, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val currentDate = simpleDateFormat.format(Date())
        fun setUpRecycler(
            recyclerView: RecyclerView,
            frame: LinearLayoutManager,
            fragmentActivity: FragmentActivity,
            appointmentList: ArrayList<AppointmentData>,
            cardType: String,
            doctorProfile: DoctorProfile
        ) {
            val appointmentData: ArrayList<AppointmentData> = ArrayList()
            for (i in 0 until appointmentList.size) {
                appointmentData.add(appointmentList[i])
            }

            val appAdapter = AppointmentAdapter(fragmentActivity, appointmentData, cardType, doctorProfile)
            recyclerView.layoutManager = frame
            recyclerView.adapter = appAdapter

        }
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
//                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
//                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
//                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun fetchDoctorDetails(activity: FragmentActivity,
                               recyclerView: RecyclerView,
                               loader: ProgressBar, cardType: String){
            loader.isVisible = true
            val doctorUid = FirebaseAuth.getInstance().uid
            var doctorProfile: DoctorProfile
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
                    recyclerView.isVisible = true
                    if (updatedPatientList.size == 0 || (updatedPatientList.size == 1  && updatedPatientList[0] == "")) {
//                        noAppointments()
                    } else {
                        patientList.filter { obj: String? -> obj == null }
                        doctorProfile.patientList = updatedPatientList
                        fetchAppointmentDetails(activity, doctorProfile, recyclerView, loader, cardType)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    loader.isVisible = false
                    Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
                }
            })
        }
//        fun noAppointments(view: DashboardFragment) {
//            view.dashboardNoAppointments?.isVisible = true
//            view.dashboardNoAppointments?.textSize = 20.toFloat()
//            view.dashboardNoAppointments?.gravity = Gravity.CENTER_VERTICAL
//            view.recyclerView.isVisible = false
//            view.loader.isVisible = false
//            view.dashboardPullToReferesh?.isRefreshing = false
//        }
        fun fetchAppointmentDetails(activity: FragmentActivity,
                                doctorProfile: DoctorProfile,
                                recyclerView: RecyclerView,
                                loader: ProgressBar,
        cardType: String) {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val appointmentDataList: ArrayList<AppointmentData> = ArrayList()
            val patientList = doctorProfile.patientList
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
                                    if(map["doctorUid"].equals(doctorProfile.doctorUid)){
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
                                    cardType,
                                    doctorProfile
                                )
                                recyclerView.layoutManager = LinearLayoutManager(
                                    activity!!
                                )
                                recyclerView.adapter = appointmentAdapter
                                loader.isVisible = false
//                                dashboardPullToReferesh?.isRefreshing = false
                            } else
                                loader.isVisible = false
                        }

                        override fun onCancelled(error: DatabaseError) {
                            loader.isVisible = false
                            Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
//                    val reference = firebaseDatabase.getReference("${patientList[i]}/")
//                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if(snapshot.exists()){
//                                val appointmentData = AppointmentData(
//                                    (snapshot.child("firstname").value.toString()
//                                        .capitalize(Locale.getDefault()) + " " + snapshot.child("lastname").value.toString()
//                                        .capitalize(Locale.ROOT)),
//                                    snapshot.child("appointmentDate").value.toString(),
//                                    snapshot.child("appointmentTime").value.toString(),
//                                    patientList[i],
//                                    snapshot.child("doctorId").value.toString(),
//                                    "abcde"
//                                )
//                                appointmentDataList.add(appointmentData)
//                                val appointmentAdapter = AppointmentAdapter(activity, appointmentDataList, cardType, doctorProfile)
//                                recyclerView.layoutManager = LinearLayoutManager(activity)
//                                recyclerView.adapter = appointmentAdapter
//                                loader.isVisible = false
//                            }else{
//                                loader.isVisible = false
//                            }
//
//                        }
//                        override fun onCancelled(error: DatabaseError) {
//                            loader.isVisible = false
//                            Toast.makeText(activity, "something went wrong.", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//
//                    })
                }
            }
        }
        fun getAge(dob: String, pattern: String): String{
            return (Calendar.getInstance().weekYear - dob.split(pattern)[2].toInt()).toString() + "Years"
        }




    }


}