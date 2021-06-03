package com.app.telehealth.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.ViewPagerAdapters
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_appointments.view.*


class AppointmentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_appointments, container, false)
        Constants.navigateFragment(
            ConfirmedAppointmentsFragment(),
            activity!!,
            R.id.appointmentFrame
        )
        setUpTabs()

        view.appointmentsTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        Constants.navigateFragment(
                            ConfirmedAppointmentsFragment(),
                            activity!!,
                            R.id.appointmentFrame
                        )
                        view?.confirmed?.setBackgroundResource(R.drawable.appointments_button)
                        view?.cancelled?.setBackgroundResource(R.drawable.appointments_button_unselected)

                    }
                    1 -> {
                        Constants.navigateFragment(
                            CancelledAppointmentsFragment(),
                            activity!!,
                            R.id.appointmentFrame
                        )
                        view?.cancelled?.setBackgroundResource(R.drawable.appointments_button)
                        view?.confirmed?.setBackgroundResource(R.drawable.appointments_button_unselected)

                    }


                }
                Log.d(Constants.APP_TAG, "Tab selected ${tab?.position}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        return view
    }

    private fun setUpTabs() {
        var tabLayout = TabLayout(activity!!)
        val adapters = ViewPagerAdapters(childFragmentManager)
        adapters.addFragments(ConfirmedAppointmentsFragment(), "Confirmed")
        adapters.addFragments(CancelledAppointmentsFragment(), "Cancelled")
    }


}