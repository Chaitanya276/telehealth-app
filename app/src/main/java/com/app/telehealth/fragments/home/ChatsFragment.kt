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


class ChatsFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val chattingModeArray = ArrayList<ChattingModel>()
        chattingModeArray.add(ChattingModel("Patient Patient1", "message one", "10:00AM", 0))
        chattingModeArray.add(ChattingModel("Patient Patient2", "message one", "10:01AM", 3))
        chattingModeArray.add(ChattingModel("Patient Patient3", "message one", "10:02AM", 5))
        chattingModeArray.add(ChattingModel("Patient Patient4", "message one", "10:03AM", 15))
        chattingModeArray.add(ChattingModel("Patient Patient5", "message one", "10:04AM", 10))
        chattingModeArray.add(ChattingModel("Patient Patient6", "message one", "10:05AM", 0))
        var chattingAdapter = ChattingAdapter(activity!!, chattingModeArray)
        view.chatsRecyclerView.layoutManager = LinearLayoutManager(activity)
        view.chatsRecyclerView.adapter = chattingAdapter

        return view
    }


}