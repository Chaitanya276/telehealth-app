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
import kotlinx.android.synthetic.main.fragment_intro2.view.*


class IntroFragment2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro2, container, false)
        view.intro2_img_page1.setOnClickListener {
            Constants.navigateFragment(IntroFragment1(), activity!!, R.id.mainActivityFrameLayout)
//            val introFragment2 = IntroFragment1()
//            val supportFragment = activity?.supportFragmentManager
//            val manager = supportFragment?.beginTransaction()
//            manager?.replace(R.id.mainActivityFrameLayout, introFragment2)
//                ?.commit()
        }
        view.intro2_img_page2.setOnClickListener {
            Constants.navigateFragment(IntroFragment2(), activity!!, R.id.mainActivityFrameLayout)
//            val introFragment2 = IntroFragment2()
//            val supportFragment = activity?.supportFragmentManager
//            val manager = supportFragment?.beginTransaction()
//            manager?.replace(R.id.mainActivityFrameLayout, introFragment2)
//                ?.commit()
        }
        view.intro2_img_page3.setOnClickListener {
//            val introFragment2 = IntroFragment3()
//            val supportFragment = activity?.supportFragmentManager
//            val manager = supportFragment?.beginTransaction()
//            manager?.replace(R.id.mainActivityFrameLayout, introFragment2)
//                ?.commit()
            Constants.navigateFragment(IntroFragment3(), activity!!, R.id.mainActivityFrameLayout)
        }
        val handler = Handler(Looper.getMainLooper())
        handler.post(Runnable {
            fun run(){

            }
        })
        handler.postDelayed({
            val introFragment2 = IntroFragment3()
            val supportFragment = activity?.supportFragmentManager
            val manager = supportFragment?.beginTransaction()
            manager?.replace(R.id.mainActivityFrameLayout, introFragment2)
                ?.commitAllowingStateLoss()
//                ?.remove(IntroFragment1())
//                ?.addToBackStack(null)
//                ?.commit()
        }, Constants.TIME_DELAY)
        return view
    }
}