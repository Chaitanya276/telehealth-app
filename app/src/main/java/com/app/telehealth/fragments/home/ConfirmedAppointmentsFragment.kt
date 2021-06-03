package com.app.telehealth.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import kotlinx.android.synthetic.main.fragment_appointments_confirmed.view.*


class ConfirmedAppointmentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_appointments_confirmed, container, false)

/*        Constants.fetchDoctorDetails(
            activity!!,
            view.confirmedAppointmentRecyclerView,
            view.confirmedAppointmentLoader,
            "confirmedAppointment"
        )*/
        return view
    }


}