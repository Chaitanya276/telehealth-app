package com.app.telehealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.app.telehealth.fragments.ForgotPasswordFragment
import com.app.telehealth.fragments.SignInFragment
import com.app.telehealth.fragments.SignUpFragment

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        var fragmentName = intent.getStringExtra("buttonName")
        Log.d("FRAGMENT", "fragment name: $fragmentName")
        initializeFrame(fragmentName.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initializeFrame(fragmentName: String) {
        var fragment: Fragment
        if(fragmentName == "signup") {
            fragment = SignUpFragment()
        }
        else if(fragmentName == "forgotpassword") {
            fragment = ForgotPasswordFragment()
        }
        else {
            fragment = SignInFragment()
        }
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.signInFrame, fragment)
            .addToBackStack(null)
            .commit()

    }
}