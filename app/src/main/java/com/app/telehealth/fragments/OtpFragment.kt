package com.app.telehealth.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.telehealth.HomeActivity
import com.app.telehealth.R
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_otp.view.*

class OtpFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_otp, container, false)
//        var phoneno  = arguments?.getString("phone")
//        Log.d("OTP FRAGMENT", "phone: $phoneno")

        view.etOTP1.addTextChangedListener(GenericTextWatcher(view.etOTP1, view.etOTP2))
        view.etOTP2.addTextChangedListener(GenericTextWatcher(view.etOTP2, view.etOTP3))
        view.etOTP3.addTextChangedListener(GenericTextWatcher(view.etOTP3, view.etOTP4))
        view.etOTP4.addTextChangedListener(GenericTextWatcher(view.etOTP4, view.etOTP5))
        view.etOTP5.addTextChangedListener(GenericTextWatcher(view.etOTP5, view.etOTP6))
        view.etOTP6.addTextChangedListener(GenericTextWatcher(view.etOTP6, null))

        view.etOTP1.setOnKeyListener(GenericKeyEvent(view.etOTP1, null))
        view.etOTP2.setOnKeyListener(GenericKeyEvent(view.etOTP2, view.etOTP1))
        view.etOTP3.setOnKeyListener(GenericKeyEvent(view.etOTP3, view.etOTP2))
        view.etOTP4.setOnKeyListener(GenericKeyEvent(view.etOTP4, view.etOTP3))
        view.etOTP5.setOnKeyListener(GenericKeyEvent(view.etOTP5, view.etOTP4))
        view.etOTP6.setOnKeyListener(GenericKeyEvent(view.etOTP6, view.etOTP5))

        view.btnVerifyOtp.setOnClickListener {
            Toast.makeText(activity, "Verifying...", Toast.LENGTH_SHORT).show()
//            var completeOtp: String =
            val otp1 = view.etOTP1.text.toString()
            val otp2 = view.etOTP2.text.toString()
            val otp3 = view.etOTP3.text.toString()
            val otp4 = view.etOTP4.text.toString()
            val otp5 = view.etOTP5.text.toString()
            val otp6 = view.etOTP6.text.toString()
            if (otpValidation(otp1, otp2, otp3, otp4, otp5, otp6)) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(activity, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    private fun sendOtp(phone: String){
//        PhoneAuthProvider.getInstance().verifyPhoneNumber()
    }

    fun otpValidation(
        otp1: String,
        otp2: String,
        otp3: String,
        otp4: String,
        otp5: String,
        otp6: String
    ): Boolean {
        var validated: Boolean = false
        if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty() || otp5.isEmpty() || otp6.isEmpty()) {
            if (otp6.isEmpty()) {
                view?.etOTP6?.requestFocus()
                view?.etOTP6?.error = "Enter OTP"
                validated = false
            }
            if (otp5.isEmpty()) {
                view?.etOTP5?.requestFocus()
                view?.etOTP5?.error = "Enter OTP"
                validated = false
            }
            if (otp4.isEmpty()) {
                view?.etOTP4?.requestFocus()
                view?.etOTP4?.error = "Enter OTP"
                validated = false
            }
            if (otp3.isEmpty()) {
                view?.etOTP3?.requestFocus()
                view?.etOTP3?.error = "Enter OTP"
                validated = false
            }
            if (otp2.isEmpty()) {
                view?.etOTP2?.requestFocus()
                view?.etOTP2?.error = "Enter OTP"
                validated = false
            }
            if (otp1.isEmpty()) {
                view?.etOTP1?.requestFocus()
                view?.etOTP1?.error = "Enter OTP"
                validated = false
            }
        } else {
            validated = true
        }
        return validated

    }

}


class GenericKeyEvent internal constructor(
    private val currentView: EditText,
    private val previousView: EditText?
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.etOTP1 && currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }
}

class GenericTextWatcher internal constructor(
    private val currentView: View,
    private val nextView: View?
) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView.id) {
            R.id.etOTP1 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etOTP2 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etOTP3 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etOTP4 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etOTP5 -> if (text.length == 1) nextView!!.requestFocus()
//            R.id.etOTP6 -> if (text.length == 1) nextView!!.requestFocus()
            //You can use etOTP4 same as above to hide the keyboard
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

}

