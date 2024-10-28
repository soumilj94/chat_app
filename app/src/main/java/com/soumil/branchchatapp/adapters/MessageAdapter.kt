package com.soumil.branchchatapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soumil.branchchatapp.R
import com.soumil.branchchatapp.data.Message
import com.soumil.branchchatapp.data.formatTimestamp

class MessageAdapter(
    private val context: Context,
    private var messages: List<Message>,
    private val onMessageClick: (userId: String) -> Unit
    ): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textID)
        val userId: TextView = itemView.findViewById(R.id.userId)

        val messageBody: TextView = itemView.findViewById(R.id.messageBody)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
        val agentID: TextView = itemView.findViewById(R.id.agentId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val message = messages[position]
        holder.id.text = message.id.toString()
        holder.userId.text = "User Id: ${message.user_id}"

        holder.messageBody.text = message.body
        holder.timestamp.text = formatTimestamp(message.timestamp)
        holder.agentID.text = if (message.agent_id !=null) "Agent ID: ${message.agent_id}" else "User ID: ${message.user_id}"

        holder.itemView.setOnClickListener {
            onMessageClick(message.user_id.toString())
        }
    }

    override fun getItemCount(): Int = messages.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateMessages(newMessages: List<Message>){
        messages = newMessages
        notifyDataSetChanged()
    }
}