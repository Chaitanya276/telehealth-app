package com.app.telehealth.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.telehealth.HomeActivity
import com.app.telehealth.R
import com.app.telehealth.models.AppointmentData
import com.app.telehealth.models.DoctorProfile
import com.app.telehealth.models.PatientProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.card_layout.view.*

class AppointmentAdapter// Constructor
    (
    private var context: FragmentActivity,
    private var appointmentData: ArrayList<AppointmentData>,
    private var cardType: String,
    private val doctorProfile: DoctorProfile
) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(view, cardType, appointmentData, doctorProfile)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        var appointmentData: AppointmentData = appointmentData.get(position)
        holder.patientName.text = appointmentData.patientName
        holder.appointmentDate.text = appointmentData.appointmentDate.toString()
        holder.appointmentTime.text = appointmentData.appointmentTime
    }

    override fun getItemCount(): Int {
        return appointmentData.size
    }

    class ViewHolder(
        itemView: View,
        cardType: String,
        appointments: ArrayList<AppointmentData>,
        doctorProfile: DoctorProfile
    ) :
        RecyclerView.ViewHolder(itemView) {
        var patientName: TextView
        var appointmentDate: TextView
        var appointmentTime: TextView
        var startAppoitment: Button
        var loader: ProgressBar

        init {
            patientName = itemView.patientName
            appointmentDate = itemView.appointmentDate
            appointmentTime = itemView.appointmentTime
            startAppoitment = itemView.startAppointment
            loader = itemView.appointmentLoader
            if (cardType == "confirmedAppointment") {
                startAppoitment.text = "Start"
            } else if (cardType == "cancelledAppointment") {
                startAppoitment.text = "Cancel"
            }
            startAppoitment.setOnClickListener {
                if (cardType == "cancelledAppointment") {
                    startAppoitment.isVisible = false
                    loader.isVisible = true
                    updateAppointment(appointments[adapterPosition].patientUid, doctorProfile)
                } else {
                    Toast.makeText(
                        itemView.context,
                        "name: ${patientName.text}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            itemView.cardView.setOnClickListener { view: View ->
                Toast.makeText(itemView.context, "name: ${patientName.text}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        private fun updateAppointment(patientUid: String, doctorProfile: DoctorProfile) {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            if (patientUid != null) {
                val reference = firebaseDatabase.getReference("$patientUid/")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
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
                            patientUid = patientUid,
                            appointmentsList = snapshot.child("appointmentsList").value.toString() as ArrayList<AppointmentData>
                        )
                        cancelAppointment(
                            patientProfile,
                            doctorProfile
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("ABCDEF", "this is database error : ${error.message}")
                        Toast.makeText(
                            itemView.context,
                            "something went wrong.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(itemView.context, "something went wrong.", Toast.LENGTH_SHORT).show()
            }

        }

        private fun cancelAppointment(
            patientProfile: PatientProfile,
            doctorProfile: DoctorProfile
        ) {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference = firebaseDatabase.getReference("${patientProfile.patientUid}/")
            reference.setValue(patientProfile)
                .addOnCompleteListener {
                    Log.d("ABCDEF", "this is patient list: ${doctorProfile.patientList}")
                    if (doctorProfile.patientList.contains(patientProfile.patientUid)) {
                        doctorProfile.patientList.remove(patientProfile.patientUid)
                        removePatientAppointment(doctorProfile)
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "Appointment cancelled.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                .addOnFailureListener {
                    startAppoitment.isVisible = true
                    loader.isVisible = false
                    Toast.makeText(itemView.context, "something went wrong.", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        private fun removePatientAppointment(doctorProfile: DoctorProfile) {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference = firebaseDatabase.getReference("${doctorProfile.doctorUid}/")
            Log.d("ABCDEF", "this is updated patient list: ${doctorProfile.patientList}")

            reference.setValue(doctorProfile)
                .addOnCompleteListener {
                    startAppoitment.isVisible = true
                    loader.isVisible = false
                    Toast.makeText(itemView.context, "Appointment cancelled.", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(itemView.context, HomeActivity::class.java)
                    intent.putExtra("setAppointmentTab", true)
                    (itemView.context as FragmentActivity).startActivity(intent)
                    (itemView.context as FragmentActivity).finish()
                }
                .addOnFailureListener {
                    startAppoitment.isVisible = true
                    loader.isVisible = false
                    Toast.makeText(itemView.context, "something went wrong.", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}
