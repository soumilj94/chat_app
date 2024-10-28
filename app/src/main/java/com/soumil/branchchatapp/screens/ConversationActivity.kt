package com.soumil.branchchatapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soumil.branchchatapp.adapters.ConversationAdapter
import com.soumil.branchchatapp.data.Message
import com.soumil.branchchatapp.api.RetrofitClient
import com.soumil.branchchatapp.databinding.ActivityConversationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class ConversationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConversationBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var conversationAdapter: ConversationAdapter
    private val messageList = mutableListOf<Message>()
    private lateinit var responseEditText: EditText
    private lateinit var sendButton: Button
    private var userId: String = ""
    private var threadId: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val messages: List<Message>? = intent.getParcelableArrayListExtra("messages")
        threadId = intent.getIntExtra("thread_id", 0)

        recyclerView = binding.recyclerViewConversation
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (messages != null){
            messageList.addAll(messages)
        }
        conversationAdapter = ConversationAdapter(this, messageList)
        recyclerView.adapter = conversationAdapter
        conversationAdapter.notifyDataSetChanged()

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", null)

        responseEditText = binding.responseEditText
        sendButton = binding.sendBtn

        userId = intent.getStringExtra("user_id") ?: ""

        sendButton.setOnClickListener {
            val responseText = responseEditText.text.toString().trim()
            if (responseText.isNotEmpty() && authToken != null){
                sendResponse(responseText, authToken)
            }
            else{
                Toast.makeText(this, "Can't send an empty response!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendResponse(responseText: String, authToken: String) {
        val newMessage = Message(
            id = 0,
            thread_id = messageList.firstOrNull()?.thread_id ?: 0,
            user_id = messageList.firstOrNull()?.user_id ?: "",
            agent_id = null,
            body = responseText,
            timestamp = ""
        )

        RetrofitClient.instance.sendMessage(authToken, newMessage).enqueue(object : Callback<Message>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(p0: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful){
                    response.body()?.let { createdMessage ->
                        messageList.add(createdMessage)
                        conversationAdapter.notifyDataSetChanged()
                        responseEditText.text.clear()
                        recyclerView.scrollToPosition(messageList.size - 1)
                    }
                }
                else{
                    Toast.makeText(this@ConversationActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<Message>, t: Throwable) {
                Log.e("ConversationActivity", "Error sending message: ${t.message}")
                Toast.makeText(this@ConversationActivity, "An error occurred: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
}