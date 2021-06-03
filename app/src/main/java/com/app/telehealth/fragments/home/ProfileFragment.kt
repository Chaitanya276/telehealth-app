package com.app.telehealth.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.models.DoctorProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {


    private var firstname: String = ""
    private var lastname: String = ""
    private var email: String = ""
    private var dateofBirth: String = ""
    private var gender: String = ""
    private var phoneno: String = ""
    private var phoneCode: String = ""
    private var address: String = ""
    private var city: String = ""
    private var state: String = ""
    private var country: String = ""
    private var postalcode: String = ""
    private var doctorUid: String = ""
    private var patientList: ArrayList<String> = ArrayList()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val uid = firebaseAuth.uid
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.profileTextFieldFirstname.errorIconDrawable = null
        view.profileTextFieldlastname.errorIconDrawable = null
        view.profileTextFieldEmail.errorIconDrawable = null
        view.profileTextFieldPhone.errorIconDrawable = null
//        view.profileTextFieldPhoneCode.errorIconDrawable = null
        val phoneCodeAdapter: ArrayAdapter<String> = ArrayAdapter(
            activity!!,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.countrycode)
        )
        val countryName: ArrayAdapter<String> = ArrayAdapter(
            activity!!,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.countryname)
        )
        view.etProfileCountry.setAdapter(countryName)
        view.etprofilePhoneCode.setAdapter(phoneCodeAdapter)

        view.genderGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.profileMale -> {
                    gender = "male"
                    Log.d(Constants.APP_TAG, "gender: $gender")
                }
                R.id.profileFemale -> {
                    gender = "female"
                    Log.d(Constants.APP_TAG, "gender: $gender")
                }

            }
        }
        fetchUserData()
        view.profileUpdateBtn.setOnClickListener {
            if (Constants.isInternetAvailable(activity!!)) {
                if (Constants.formValidation(
                        view.etProfileName, view.profileTextFieldFirstname,
                        view.etProfileLastname, view.profileTextFieldlastname,
                        view.etProfilePhone, view.profileTextFieldPhone,
                        view.etProfileEmail, view.profileTextFieldEmail,
                    )
                ) {
                    firstname = view.etProfileName.text.toString()
                    lastname = view.etProfileLastname.text.toString()
                    email = view.etProfileEmail.text.toString()
                    dateofBirth = view.etProfiledob.text.toString()
                    phoneno = view.etProfilePhone.text.toString()
                    phoneCode = view.etprofilePhoneCode.text.toString()
                    address = view.etProfileAdress.text.toString()
                    city = view.etProfileCity.text.toString()
                    state = view.etProfileState.text.toString()
                    country = view.etProfileCountry.text.toString()
                    postalcode = view.etProfilePostalcode.text.toString()
                    if (gender.isBlank())
                        gender = ""
                    sendUserData()
                    Log.d(Constants.APP_TAG, "gender: $gender")
                    Log.d(Constants.APP_TAG, "gender: $phoneno")
                }
            } else {
                Toast.makeText(activity, "Internet unavailable", Toast.LENGTH_SHORT).show()
            }
        }

        view.profileBackBtn.setOnClickListener {

        }

        return view
    }


    fun fetchUserData() {
        view?.profileProgressLoader?.isVisible = true
        val databaseReference = firebaseDatabase.getReference("$uid/")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(Constants.APP_TAG, "this is user profile--->  ${dataSnapshot.value}")
                val userProfile: HashMap<String, String> =
                    dataSnapshot.value as HashMap<String, String>
                val newPatientList = ArrayList<String>()
                newPatientList.addAll(dataSnapshot.child("patientList").value as Collection<String>)
                patientList.addAll(newPatientList)
                doctorUid = userProfile["doctorUid"] as String
                view?.etProfileName?.setText(userProfile["firstname"])
                view?.etProfileLastname?.setText(userProfile["lastname"])
                view?.etProfilePhone?.setText(userProfile["contact"])
                view?.etProfileEmail?.setText(userProfile["email"])
                if (!userProfile["dob"].isNullOrBlank() || !userProfile["dob"]
                        .isNullOrEmpty()
                ) {
                    view?.etProfiledob?.setText(userProfile["dob"])
                }
                if (!userProfile["gender"].isNullOrBlank() || !userProfile["gender"]
                        .isNullOrEmpty()
                ) {
                    if (userProfile["gender"] == "male")
                        view?.profileMale?.isChecked = true
                    else
                        view?.profileFemale?.isChecked = true
                }
                if (!userProfile["contact"].isNullOrBlank() || !userProfile["contact"]
                        .isNullOrEmpty()
                ) {
                    var phoneArray = userProfile["contact"]?.trim()?.split(" ")
                    if (phoneArray!!.size == 2) {
                        view?.etprofilePhoneCode?.setText(phoneArray[0])
                        view?.etProfilePhone?.setText(phoneArray[1])
                    } else {
                        view?.etProfilePhone?.setText(userProfile["contact"])
                    }

                }
                if (!userProfile["address"].isNullOrBlank() || !userProfile["address"]
                        .isNullOrEmpty()
                ) {
                    view?.etProfileAdress?.setText(userProfile["address"])
                }
                if (!userProfile["city"].isNullOrBlank() || !userProfile["city"]
                        .isNullOrEmpty() || userProfile["city"] != " "
                ) {
                    view?.etProfileCity?.setText(userProfile["city"])
                }
                if (!userProfile["state"].isNullOrBlank() || !userProfile["state"]
                        .isNullOrEmpty()
                ) {
                    view?.etProfileState?.setText(userProfile["state"])
                }
                if (!userProfile["country"].isNullOrBlank() || !userProfile["country"]
                        .isNullOrEmpty()
                ) {
                    view?.etProfileCountry?.setText(userProfile["country"])
                }
                if (!userProfile["postal"].isNullOrBlank() || !userProfile["postal"]
                        .isNullOrEmpty()
                ) {
                    view?.etProfilePostalcode?.setText(userProfile["postal"])
                }

                view?.profileProgressLoader?.isVisible = false

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, databaseError.code, Toast.LENGTH_SHORT).show()
                view?.profileProgressLoader?.isVisible = false
            }
        })
    }

    fun sendUserData() {
        view?.profileProgressLoader?.isVisible = true
        val doctorProfile = DoctorProfile(
            firstname,
            lastname,
            email,
            "$phoneCode $phoneno",
            dateofBirth,
            gender,
            address,
            city,
            state,
            country,
            postalcode,
            patientList = patientList,
            doctorUid = doctorUid
        )
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("$uid/")
        databaseReference.setValue(doctorProfile)
            .addOnCompleteListener {
                view?.profileProgressLoader?.isVisible = false
                Toast.makeText(activity!!, "Profile updated successfully.", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(activity!!, "something went wrong.", Toast.LENGTH_SHORT).show()
                view?.profileProgressLoader?.isVisible = false

            }
        view?.profileProgressLoader?.isVisible = false
    }


}

/*
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)


        view.homeProfile.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null){
                Log.d(Constants.APP_TAG, "firebase logout")
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            if(googleSignInClient.signInIntent != null){
                Log.d(Constants.APP_TAG, "gmail logout")
                googleSignInClient.signOut()
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }

        }

                /*val gson = Gson()
        val phoneCodeList = object : TypeToken<Array<PhoneCode>>() {}.type
        val codeList: Array<PhoneCode> = gson.fromJson(phonejson, phoneCodeList)
        var phonelist = ArrayList<String>()

        codeList.forEachIndexed{ idx, tut ->
            phonelist.add(idx, tut.toString())
        }
        Log.d(Constants.APP_TAG, phonelist.toString())
        Log.d(Constants.APP_TAG, "list size: ${phonelist.size} ")
        */
 */