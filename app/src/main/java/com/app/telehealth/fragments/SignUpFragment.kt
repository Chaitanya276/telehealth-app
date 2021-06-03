package com.app.telehealth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.SignInActivity
import com.app.telehealth.models.DoctorProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlin.collections.ArrayList


class SignUpFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private var firstname: String = ""
    private var lastname: String = ""
    private var email: String = ""
    private var contact: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        view.textFieldFirstname.errorIconDrawable = null
        view.textFieldlastname.errorIconDrawable = null
        view.textFieldEmail.errorIconDrawable = null
        view.textFieldPhone.errorIconDrawable = null
        view.textFieldPassword.errorIconDrawable = null
        view.textFieldConfirmPassword.errorIconDrawable = null
        view.btnRegister.setOnClickListener {
            if (Constants.isInternetAvailable(activity!!)) {
                if (Constants.formValidation(
                        view.etRegisterName, view.textFieldFirstname,
                        view.etRegisterLastname, view.textFieldlastname,
                        view.etRegisterPhone, view.textFieldPhone,
                        view.etRegisterEmail, view.textFieldEmail,
                        view.etRegisterPassword, view.textFieldPassword,
                        view.etRegisterConfirmPassword, view.textFieldConfirmPassword,
                    )
                ) {
                    firstname = view.etRegisterName.text.toString().trim()
                    lastname = view.etRegisterLastname.text.toString().trim()
                    email = view.etRegisterEmail.text.toString().trim()
                    contact = view.etRegisterPhone.text.toString().trim()
                    password = view.etRegisterPassword.text.toString().trim()
                    confirmPassword = view.etRegisterConfirmPassword.text.toString().trim()
                    view.signUpLoader.isVisible = true
                    registerUser(email, password)
                }
            } else {
                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }

        }

        view.tvRegisterSignIn.setOnClickListener {

            val intent = Intent(activity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        view.tvRegisterTermsAndConditions.setOnClickListener {
            Constants.navigateFragment(
                TermsConditionsFragment(),
                activity!!,
                R.id.signInFrame,
                true
            )
        }

        return view
    }

    fun registerUser(email: String, password: String) {
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            activity,
                            "Verification mail has been send successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        firebaseUser.sendEmailVerification()
                        sendUserData(firstname, lastname, email, contact)
                    }
                }
                .addOnFailureListener {
                    view?.signUpLoader?.isVisible = false
                    if (it.message.toString().contains("email address is already in use")) {
                        Toast.makeText(
                            activity,
                            "Email already registered, please sign in",
                            Toast.LENGTH_SHORT
                        ).show()
                        view?.signUpLoader?.isVisible = false
                        val intent = Intent(activity, SignInActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        Log.d("TELEHEALTH", "if statement sign up error: ${it.message}")

                    } else {
                        Log.d("TELEHEALTH", "else sign up error: ${it.localizedMessage}")
                        Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Log.d("user email", "error: ${e.toString()}  \n error: ${e.localizedMessage} ")
        }


    }

    private fun sendUserData(firstname: String, lastname: String, email: String, contact: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().uid
        val reference = firebaseDatabase.getReference("$uid/")
        val patientList = ArrayList<String>()
        patientList.add("")
        val doctorProfile = DoctorProfile(firstname, lastname, email, "+91 $contact",patientList = patientList, doctorUid = uid.toString())
        reference.setValue(doctorProfile).addOnSuccessListener {
            addDoctor(uid.toString())
        }
            .addOnFailureListener {
                view?.signUpLoader?.isVisible = false
                Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
                Log.d("ERRORFIRE", it.toString())
            }

    }
    private fun addDoctor(doctorUid: String){
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val doctorList = arrayListOf<String>()
        val databaseReference = firebaseDatabase.getReference("doctor/")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    doctorList.add(i.value.toString())
                }
                Log.d(Constants.APP_TAG, "this is doctor list: $doctorList")
                updateDoctorList(doctorUid, doctorList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(Constants.APP_TAG, "this is empty doctor list")
            }

        })
    }
    private fun updateDoctorList(newDoctorUid: String, doctorList: ArrayList<String>){
        doctorList.add(newDoctorUid)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference("doctor/")
        reference.setValue(doctorList).addOnSuccessListener {
            view?.signUpLoader?.isVisible = false
            view?.signUpLoader?.isVisible = false
            val intent = Intent(activity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
            .addOnFailureListener {
                view?.signUpLoader?.isVisible = false
                Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
                Log.d("ERRORFIRE", it.toString())
            }
    }


}


