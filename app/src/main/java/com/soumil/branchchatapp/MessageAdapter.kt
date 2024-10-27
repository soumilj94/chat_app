package com.soumil.branchchatapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soumil.branchchatapp.api.Message

class MessageAdapter(private val messages: List<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textID)
        val agentID: TextView = itemView.findViewById(R.id.agentId)
        val messageBody: TextView = itemView.findViewById(R.id.messageBody)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
        val userId: TextView = itemView.findViewById(R.id.userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.id.text = message.id.toString()
        holder.agentID.text = "Agent ID: ${message.agent_id}"
        holder.messageBody.text = message.body
        holder.timestamp.text = message.timestamp
        holder.userId.text = "User Id: ${message.user_id}"
    }
}