package com.app.telehealth.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.fragments.patient.BookAppointmentSelectDateFragment
import com.app.telehealth.models.DoctorProfile
import com.app.telehealth.models.PatientProfile
import kotlinx.android.synthetic.main.book_appointment_card_layout.view.*
import java.util.*


class BookAppointmentAdapter
    (private var context: FragmentActivity, var doctorData: ArrayList<DoctorProfile>, var patientData: PatientProfile) :
    RecyclerView.Adapter<BookAppointmentAdapter.Viewholder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        // to inflate the layout for each item of recycler view.
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.book_appointment_card_layout,
            parent,
            false
        )
        return Viewholder(view, doctorData, patientData)
    }

    override fun onBindViewHolder(@NonNull holder: Viewholder, position: Int) {
        val doctorData: DoctorProfile = doctorData.get(position)
        val fullname = "${doctorData.firstname.capitalize(Locale.ROOT)} ${doctorData.lastname.capitalize(Locale.ROOT)}"
        holder.bookAppointmentName.text = fullname
        if ((!doctorData.dob.isNullOrBlank()) && (doctorData.dob != "")) {
            val year = doctorData.dob.split("-")
            if (year[2] != null) {
                holder.bookAppointmentAge.text = Constants.getAge(doctorData.dob, "-")
            }
        } else {
            holder.bookAppointmentAge.text = doctorData.dob
        }
        holder.bookAppointmentGender.text = doctorData.gender.capitalize()

    }

    override fun getItemCount(): Int {
        return doctorData.size
    }


    class Viewholder(itemView: View, doctorData: ArrayList<DoctorProfile>, patientData: PatientProfile) :
        RecyclerView.ViewHolder(itemView) {
        var bookAppointmentName: TextView = itemView.bookAppointmensName
        var bookAppointmentAge: TextView = itemView.bookAppointmensAge
        var bookAppointmentGender: TextView = itemView.bookAppointmensGender
        var bookAppointmentBook: Button = itemView.bookAppointmentBook

        init {
            bookAppointmentBook.setOnClickListener {
                val bookAppointmentSelectDateFragment = BookAppointmentSelectDateFragment()
                val bundle = Bundle()
                bundle.putSerializable("doctorProfileObject", doctorData[adapterPosition])
                bundle.putSerializable("patientProfileObject", patientData)
                bundle.putString("name", bookAppointmentName.text.toString())
                bundle.putString("age", bookAppointmentAge.text.toString())
                bundle.putString("gender", bookAppointmentGender.text.toString())
                bookAppointmentSelectDateFragment.arguments = bundle
                (it.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.patientLayout, bookAppointmentSelectDateFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }


}