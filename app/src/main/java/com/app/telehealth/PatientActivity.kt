package com.app.telehealth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.telehealth.fragments.patient.PatientsFragment
import kotlinx.android.synthetic.main.activity_patient.*

class PatientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        val fragment = PatientsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.patientLayout, fragment)
            .commit()

    }
}