package com.app.telehealth.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import kotlinx.android.synthetic.main.fragment_appointments_cancelled.view.*


class CancelledAppointmentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_appointments_cancelled, container, false)
        Constants.fetchDoctorDetails(
            activity!!,
            view.cancelledAppointmentRecyclerView,
            view.cancelledAppointmentLoader,
            "cancelledAppointment"
        )
        return view
    }
}