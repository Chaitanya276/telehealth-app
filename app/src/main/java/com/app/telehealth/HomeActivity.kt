package com.app.telehealth

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.telehealth.fragments.home.AppointmentsFragment
import com.app.telehealth.fragments.home.ChatsFragment
import com.app.telehealth.fragments.home.DashboardFragment
import com.app.telehealth.fragments.home.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity() : AppCompatActivity() {
    private var pressedTime: Long = 0
    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true)
            //super.onBackPressed()
        } else {
            Toast.makeText(baseContext, "Press back again to exit.", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val setAppointmentTab: Boolean = intent.getBooleanExtra("setAppointmentTab", false)
        Log.d("ABCDEF", "set appointment tab? $setAppointmentTab")
        setUpTabs(setAppointmentTab)

/*        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto = acct.photoUrl

        }*/

    }

    private fun setUpTabs(setAppointmentTab: Boolean) {
        val adapter = ViewPagerAdapters(supportFragmentManager)
        adapter.addFragments(DashboardFragment(), "")
        adapter.addFragments(AppointmentsFragment(), "")
        adapter.addFragments(ChatsFragment(), "")
        adapter.addFragments(ProfileFragment(), "")
        homeViewPager.adapter = adapter
        homeTabs.setupWithViewPager(homeViewPager)
        homeTabs.getTabAt(0)!!.setIcon(R.drawable.home)
        homeTabs.getTabAt(1)!!.setIcon(R.drawable.appointments)
        homeTabs.getTabAt(2)!!.setIcon(R.drawable.chat)
        homeTabs.getTabAt(3)!!.setIcon(R.drawable.profile)
        if (setAppointmentTab) {
            homeTabs.getTabAt(1)!!.select()
        }
    }

}