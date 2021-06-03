package com.app.telehealth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.telehealth.HomeActivity
import com.app.telehealth.R
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class ResetPasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        view.btnResetPassword.setOnClickListener {
            val resetPassword = view.etResetPassword.text.toString().trim()
            val resetConfirmPassword = view.etResetConfirmPassword.text.toString().trim()
            if (resetPassword.equals(resetConfirmPassword)) {
                Toast.makeText(activity, "Password and confirm password matched", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Check passwords", Toast.LENGTH_SHORT).show()
            }
        }




        return view
    }

}