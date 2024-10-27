package com.soumil.branchchatapp.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.soumil.branchchatapp.MainActivity
import com.soumil.branchchatapp.R
import com.soumil.branchchatapp.databinding.ActivityInitialCheckBinding

class InitialCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitialCheckBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sp.getString("auth_token", null)

        if (token != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
        else{
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }
}