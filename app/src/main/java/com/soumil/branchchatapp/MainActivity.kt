package com.soumil.branchchatapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soumil.branchchatapp.api.Message
import com.soumil.branchchatapp.api.RetrofitClient
import com.soumil.branchchatapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter

        val sp: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sp.getString("auth_token", null)

        binding.token.text = "auth_token: $token"

        if (token != null){
            fetchMessages(token)
        }
        else{
            Toast.makeText(this, "Token Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMessages(token: String) {
        RetrofitClient.instance.getMessages(token).enqueue(object : Callback<List<Message>>{

            override fun onResponse(p0: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful){
                    response.body()?.let { messages ->
                        messageList.clear()
                        messageList.addAll(messages)
                        messageAdapter.notifyDataSetChanged()
                    }
                }
                else{
                    Toast.makeText(this@MainActivity, "Failed to retrieve messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<List<Message>>, t: Throwable) {
                Log.e("MainActivity", "Error fetching messages: ${t.message}")
                Toast.makeText(this@MainActivity, "An error occurred: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
}