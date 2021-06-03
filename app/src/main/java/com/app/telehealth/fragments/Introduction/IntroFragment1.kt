package com.app.telehealth.fragments.Introduction

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.telehealth.Constants
import com.app.telehealth.R
import com.app.telehealth.fragments.SignUpFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_intro1.view.*
import kotlinx.android.synthetic.main.fragment_intro2.view.*
import java.util.*


class IntroFragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_intro1, container, false)
        view.intro1_img_page1.setOnClickListener {
            Constants.navigateFragment(IntroFragment1(), activity!!, R.id.mainActivityFrameLayout)

        }
        view.intro1_img_page2.setOnClickListener {

            Constants.navigateFragment(IntroFragment2(), activity!!, R.id.mainActivityFrameLayout)
        }
        view.intro1_img_page3.setOnClickListener {

            Constants.navigateFragment(IntroFragment3(), activity!!, R.id.mainActivityFrameLayout)
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            val introFragment2 = IntroFragment2()
            val supportFragment = activity?.supportFragmentManager
            val manager = supportFragment?.beginTransaction()
            manager?.replace(R.id.mainActivityFrameLayout, introFragment2)
                ?.commitAllowingStateLoss()
        },  Constants.TIME_DELAY)

        return view


    }
}