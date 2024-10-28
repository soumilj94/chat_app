package com.soumil.branchchatapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Message(
    val id: Int,
    val thread_id: Int,
    val user_id: String?,
    val agent_id: Int?,
    val body: String,
    val timestamp: String
): Parcelable

fun formatTimestamp(timestamp: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val formatter = SimpleDateFormat("dd-MM-yyyy (HH:mm)", Locale.getDefault())
    return try {
        formatter.format(parser.parse(timestamp)!!)
    } catch (e: Exception) {
        e.printStackTrace()
        "Invalid date"
    }
}

fun getLatestMessagesByUser(messages: List<Message>): List<Message> {
    return messages
        .groupBy { it.user_id }
        .map { (_, userMessages) -> userMessages.maxByOrNull { it.id }!! }
}