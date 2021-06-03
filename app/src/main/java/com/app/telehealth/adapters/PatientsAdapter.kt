package com.app.telehealth.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.telehealth.R
import com.app.telehealth.fragments.patient.BookAppointmentFragment
import com.app.telehealth.fragments.patient.PatientDetailsFragment
import com.app.telehealth.models.PatientProfile
import kotlinx.android.synthetic.main.patient_card_layout.view.*

class PatientsAdapter// Constructor
    (private var context: FragmentActivity, var patientsData: ArrayList<PatientProfile>) :
    RecyclerView.Adapter<PatientsAdapter.Viewholder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.patient_card_layout,
            parent,
            false
        )
        return Viewholder(view, patientsData)
    }

    override fun onBindViewHolder(@NonNull holder: Viewholder, position: Int) {
        val patientsData: PatientProfile = patientsData.get(position)
        holder.patientsName.text =
            "${patientsData.firstname.capitalize()} ${patientsData.lastname.capitalize()}"
        holder.patientsAge.text = patientsData.dob
        holder.patientsGender.text = patientsData.gender.capitalize()

    }

    override fun getItemCount(): Int {
        return patientsData.size
    }


    class Viewholder(itemView: View, patientsData: ArrayList<PatientProfile>) :
        RecyclerView.ViewHolder(
            itemView
        ) {
        var patientsName: TextView = itemView.patientsName
        var patientsAge: TextView = itemView.patientsAge
        var patientsGender: TextView = itemView.patientsGender
        var patientsViewDetails: Button = itemView.patientsViewDetails
        var patientsBook: Button = itemView.patientsBookAppointment

        init {
            patientsBook.setOnClickListener {
                val bookAppointment = BookAppointmentFragment()
                val bundle = Bundle()
                bundle.putSerializable("patientDetailsObject", patientsData[adapterPosition])
                val patientUid = patientsData[adapterPosition].patientUid
                val preferences = (it.context as FragmentActivity).getSharedPreferences(
                    "SHARED",
                    Context.MODE_PRIVATE
                )
                val editor = preferences.edit()
                editor.clear()
                editor.putString("patientUid", patientUid)
                editor.apply()
                editor.commit()
                bookAppointment.arguments = bundle
                (it.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.patientLayout, bookAppointment)
                    .addToBackStack(null)
                    .commit()
            }
            patientsViewDetails.setOnClickListener { view: View ->
                val patientDetailsFragment = PatientDetailsFragment()
                val bundle = Bundle()
                bundle.putSerializable("patientDetailsObject", patientsData[adapterPosition])
                patientDetailsFragment.arguments = bundle
                (view.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.patientLayout, patientDetailsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

}