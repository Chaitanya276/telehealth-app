package com.app.telehealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.telehealth.R
import com.app.telehealth.models.ChattingModel
import kotlinx.android.synthetic.main.chatting_card.view.*

class ChattingAdapter// Constructor
    (
    private var context: FragmentActivity,
    private var chattingData: ArrayList<ChattingModel>,
) : RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chatting_card, parent, false)
        return ViewHolder(view, chattingData)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        var chattingData: ChattingModel = chattingData.get(position)
        holder.chattingPatientName.text = chattingData.patientName
        holder.chattingRecentMessage.text = chattingData.recentMessage
        holder.chattingRecentMessageTime.text = chattingData.messageTime
        holder.chattingMessageCount.text = chattingData.messageCount.toString()
    }

    override fun getItemCount(): Int {
        return chattingData.size
    }

    class ViewHolder(
        itemView: View,
        chattingData: ArrayList<ChattingModel>,
    ) :
        RecyclerView.ViewHolder(itemView) {
        var chattingPatientName: TextView = itemView.chattingPatientName
        var chattingRecentMessage: TextView = itemView.chattingRecentMessage
        var chattingRecentMessageTime: TextView = itemView.chattingRecentMessageTime
        var chattingMessageCount: TextView = itemView.chattingMessageCount
        var chattingCard: CardView = itemView.chattingCard

        init {
            chattingCard.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "name: ${chattingData[adapterPosition].patientName}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}
