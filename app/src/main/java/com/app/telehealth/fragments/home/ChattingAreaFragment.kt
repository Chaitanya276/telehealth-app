package com.app.telehealth.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.telehealth.R
import com.app.telehealth.adapters.ChattingAdapter
import com.app.telehealth.models.ChattingModel
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChattingAreaFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_chatting_area, container, false)


        return view
    }


}