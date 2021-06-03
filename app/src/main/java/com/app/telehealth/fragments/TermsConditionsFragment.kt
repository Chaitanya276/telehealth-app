package com.app.telehealth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.telehealth.Constants
import com.app.telehealth.R
import kotlinx.android.synthetic.main.fragment_terms_conditions.view.*


class TermsConditionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_terms_conditions, container, false)


        view.termsBackButton.setOnClickListener{
            Constants.navigateFragment(SignUpFragment(), activity!!, R.id.signInFrame)
//            val signUpFragment = SignUpFragment()
//            val manager = activity?.supportFragmentManager
//            val transaction = manager?.beginTransaction()
//            transaction?.replace(R.id.signInFrame, signUpFragment)?.commit()
        }
        return view
    }

}