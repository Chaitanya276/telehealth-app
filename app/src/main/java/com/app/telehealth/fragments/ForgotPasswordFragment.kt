package com.app.telehealth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class ForgotPasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        view.textFieldForgotPasswordEmailLayout.errorIconDrawable = null

        view.tvForgotSignUp.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            intent.putExtra("buttonName", "signup")
            startActivity(intent)
            activity?.finish()
        }

        view.tvForgotSignIn.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        view.btnForgotPassword.setOnClickListener {
            if (Constants.isInternetAvailable(activity!!)) {
                if (Constants.formValidation(
                        emailET = view.etForgotPasswordEmail, emailLayout = view.textFieldForgotPasswordEmailLayout
                )) {
                    view.forgotPasswordLoader.isVisible = true
                    val resetEmail = view.etForgotPasswordEmail.text.toString().trim()
                    FirebaseAuth.getInstance().sendPasswordResetEmail(resetEmail)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    "Reset password mail has been sent successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                view.forgotPasswordLoader.isVisible = false
                                val intent = Intent(activity, SignInActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener {
                            view.forgotPasswordLoader.isVisible = false
                            Toast.makeText(
                                activity,
                                "No user registered with this email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            } else {
                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


}