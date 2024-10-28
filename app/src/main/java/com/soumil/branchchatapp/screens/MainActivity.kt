package com.soumil.branchchatapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.soumil.branchchatapp.adapters.MessageAdapter
import com.soumil.branchchatapp.data.Message
import com.soumil.branchchatapp.api.RetrofitClient
import com.soumil.branchchatapp.data.getLatestMessagesByUser
import com.soumil.branchchatapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

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
        messageAdapter = MessageAdapter(this, messageList){ userId ->
            val filteredMessages = messageList.filter { it.user_id.toString() == userId }.sortedBy { it.id }

            val intent = Intent(this, ConversationActivity::class.java)
            intent.putParcelableArrayListExtra("messages", ArrayList(filteredMessages))
            startActivity(intent)
        }
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

        binding.logOutBtn.setOnClickListener {
            showLogOutDialog()
        }
    }

    private fun fetchMessages(token: String) {
        RetrofitClient.instance.getMessages(token).enqueue(object : Callback<List<Message>>{

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(p0: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful){
                    response.body()?.let { messages ->
                        messageList.clear()
                        messageList.addAll(getLatestMessagesByUser(messages))
                        messageAdapter.updateMessages(messageList)
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

    private fun showLogOutDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog
            .setTitle("Log out?")
            .setMessage("Do you want to log out this session?")
            .setPositiveButton("Yes"){it, _ ->
                logOut()
                it.dismiss()
            }
            .setNegativeButton("No"){it, _ ->
                it.dismiss()
            }
            .create()
            .show()
    }

    private fun logOut() {
        val sp: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sp.edit().clear().apply()

        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finishAffinity()
    }
}