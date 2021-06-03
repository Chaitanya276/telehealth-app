package com.app.telehealth.fragments.patient

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.models.AppointmentData
import com.app.telehealth.models.DoctorProfile
import com.app.telehealth.models.PatientProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_book_appointment_select_date.*
import kotlinx.android.synthetic.main.fragment_book_appointment_select_date.view.*
import java.time.Month

class BookAppointmentSelectDateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var doctorProfile: DoctorProfile
    lateinit var patientProfile: PatientProfile
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var time: String? = null
        val view =
            inflater.inflate(R.layout.fragment_book_appointment_select_date, container, false)
        doctorProfile = arguments?.getSerializable("doctorProfileObject") as DoctorProfile
        patientProfile = arguments?.getSerializable("patientProfileObject") as PatientProfile
        doctorProfile.patientList.toSet().toList()
        doctorProfile.patientList.filter { obj: String? -> obj == null }
        Log.d(
            "ABCDEF",
            "this is patient list from the doctor profile ${
                doctorProfile.patientList.toSet().toList().size
            }"
        )
        var appointmentsList = ArrayList<String>()
        Log.d("ABCDEF", "parced doctor patient list: ${doctorProfile.patientList}")
        if(doctorProfile.patientList.size != 0){
            doctorProfile.patientList.filter{ obj: String? -> obj == null}
            appointmentsList = fetchDoctorsAppointments(
                // Because java.util.Collections$SingletonList cannot be cast to java.util.ArrayList
                if(doctorProfile.patientList.size > 1){
                    doctorProfile.patientList.toSet().toList() as ArrayList<String>
                }else{
                    doctorProfile.patientList as ArrayList<String>
                }

            )
        }

        Log.d("ABCDEF", "recieved appointment list: $appointmentsList")
        if (patientProfile.appointmentsList.size != 0) {
            val list = patientProfile.appointmentsList as ArrayList<*>
            for (i in list.indices) {
                val appointment: AppointmentData = list[i] as AppointmentData
                when (appointment.appointmentTime) {
                    "9:00" -> {
                        view?.nine?.isEnabled = false
                        view?.nine?.setTextColor(Color.GRAY)
                    }
                    "9:30" -> {
                        view?.nineThirty?.isEnabled = false
                        view?.nineThirty?.setTextColor(Color.GRAY)
                    }
                    "10:00" -> {
                        view?.ten?.isEnabled = false
                        view?.ten?.setTextColor(Color.GRAY)
                    }
                    "10:30" -> {
                        view?.tenThirty?.isEnabled = false
                        view?.tenThirty?.setTextColor(Color.GRAY)
                    }
                    "11:00" -> {
                        view?.eleven?.isEnabled = false
                        view?.eleven?.setTextColor(Color.GRAY)
                    }
                    "11:30" -> {
                        view?.elevenThirty?.isEnabled = false
                        view?.elevenThirty?.setTextColor(Color.GRAY)
                    }
                    "12:00" -> {
                        view?.tweleve?.isEnabled = false
                        view?.tweleve?.setTextColor(Color.GRAY)
                    }
                }
            }
        }
        view.nine.setOnClickListener { time = makeBold(0) }
        view.nineThirty.setOnClickListener { time = makeBold(1) }
        view.ten.setOnClickListener { time = makeBold(2) }
        view.tenThirty.setOnClickListener { time = makeBold(3) }
        view.eleven.setOnClickListener { time = makeBold(4) }
        view.elevenThirty.setOnClickListener { time = makeBold(5) }
        view.tweleve.setOnClickListener { time = makeBold(6) }
        view.bookAppointmentSelectDate.minDate = System.currentTimeMillis() - 1000
        view.bookAppointmentSelectDate.setOnDateChangedListener { datePicker, i, i2, i3 ->
            Log.d(Constants.APP_TAG, "changed date: $i, $i2 $i3")
        }
        view.bookAppointmensSelectDateNext.setOnClickListener {
            if (time == null) {
                Toast.makeText(activity!!, "Select appointment time.", Toast.LENGTH_SHORT).show()
            } else {
                val reviewAppointmentFragment = ReviewAppointmentFragment()
                val day = view.bookAppointmentSelectDate.dayOfMonth
                val month = view.bookAppointmentSelectDate.month + 1
                val year = view.bookAppointmentSelectDate.year
                val actualmonth = Month.of(month)
                Log.d(Constants.APP_TAG, "changed date: $day, $month $year   $actualmonth")
                val bundle = Bundle()
                bundle.putSerializable("doctorProfileObject", doctorProfile)
                bundle.putSerializable("patientProfileObject", patientProfile)
                bundle.putString("appointmentTime", time)
                bundle.putString("appointmentDate", "$day-$actualmonth-$year")
                reviewAppointmentFragment.arguments = bundle
                val supportFragment = activity!!.supportFragmentManager
                supportFragment.beginTransaction()
                    .replace(R.id.patientLayout, reviewAppointmentFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        return view
    }

    private fun fetchDoctorsAppointments(patientList: ArrayList<String>): ArrayList<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        var appointmentsList = ArrayList<String>()
        view?.bookAppointmentSelectDateLoader?.isVisible = true
        for (i in patientList.indices) {
            if (patientList[i].isNotBlank() && patientList[i].isNotEmpty()) {
                val databaseReference =
                    firebaseDatabase.getReference("${patientList[i]}/appointmentsList")
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var list = snapshot.value as ArrayList<*>
                            for (i in list.indices) {
                                val map: Map<String, String> = list[i] as Map<String, String>
                                appointmentsList.add(map["appointmentTime"].toString())
                            }
                            view?.bookAppointmentSelectDateLoader?.isVisible = false
                        } else {
                            view?.bookAppointmentSelectDateLoader?.isVisible = false
                        }
                        Log.d(
                            "ABCDEF",
                            "this is final appointment list from doctor side: $appointmentsList"
                        )
                        if(appointmentsList.size != 0){
                            for (i in appointmentsList.indices) {
                                when (appointmentsList[i]) {
                                    "9:00" -> {
                                        view?.nine?.isEnabled = false
                                        view?.nine?.setTextColor(Color.GRAY)
                                    }
                                    "9:30" -> {
                                        view?.nineThirty?.isEnabled = false
                                        view?.nineThirty?.setTextColor(Color.GRAY)
                                    }
                                    "10:00" -> {
                                        view?.ten?.isEnabled = false
                                        view?.ten?.setTextColor(Color.GRAY)
                                    }
                                    "10:30" -> {
                                        view?.tenThirty?.isEnabled = false
                                        view?.tenThirty?.setTextColor(Color.GRAY)
                                    }
                                    "11:00" -> {
                                        view?.eleven?.isEnabled = false
                                        view?.eleven?.setTextColor(Color.GRAY)
                                    }
                                    "11:30" -> {
                                        view?.elevenThirty?.isEnabled = false
                                        view?.elevenThirty?.setTextColor(Color.GRAY)
                                    }
                                    "12:00" -> {
                                        view?.tweleve?.isEnabled = false
                                        view?.tweleve?.setTextColor(Color.GRAY)
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("ABCDEF", "this is database error : ${error.message}")
                        Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT)
                            .show()
                        view?.bookAppointmentSelectDateLoader?.isVisible = false
                    }
                })
            }
        }
        Log.d("ABCDEF", "this is final appointment list from doctor side end: $appointmentsList")
        view?.bookAppointmentSelectDateLoader?.isVisible = false
        return appointmentsList
    }

    private fun makeBold(position: Int): String {
        val arrayOfAllText = mutableListOf<TextView>()
        arrayOfAllText.add(activity!!.nine)
        arrayOfAllText.add(activity!!.nineThirty)
        arrayOfAllText.add(activity!!.ten)
        arrayOfAllText.add(activity!!.tenThirty)
        arrayOfAllText.add(activity!!.eleven)
        arrayOfAllText.add(activity!!.elevenThirty)
        arrayOfAllText.add(activity!!.tweleve)
        val time = arrayOfAllText[position].text.toString().trim()
        arrayOfAllText[position].typeface = Typeface.DEFAULT_BOLD
//        arrayOfAllText[position].setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.toFloat())
        arrayOfAllText.removeAt(position)
        for (i in arrayOfAllText.indices) {
            arrayOfAllText[i].typeface = Typeface.DEFAULT
//            arrayOfAllText[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.toFloat())
        }
        return time
    }

}