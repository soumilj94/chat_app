package com.soumil.branchchatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soumil.branchchatapp.R
import com.soumil.branchchatapp.data.Message
import com.soumil.branchchatapp.data.formatTimestamp

class ConversationAdapter(
    private val context: Context,
    private val messages: List<Message>
):RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val messageBody: TextView = itemView.findViewById(R.id.chatMessageBody)
        val timestamp: TextView = itemView.findViewById(R.id.chatTimestamp)
        val senderInfo: TextView = itemView.findViewById(R.id.chatSenderInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chats, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val message = messages[position]
        holder.messageBody.text = message.body
        holder.timestamp.text = formatTimestamp(message.timestamp)
        holder.senderInfo.text = if (message.agent_id != null) "Agent ID: ${message.agent_id}" else "User ID: ${message.user_id}"
    }

    override fun getItemCount(): Int = messages.size
}