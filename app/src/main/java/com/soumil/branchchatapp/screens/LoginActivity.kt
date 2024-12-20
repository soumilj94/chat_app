package com.soumil.branchchatapp.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.soumil.branchchatapp.data.LoginRequest
import com.soumil.branchchatapp.data.LoginResponse
import com.soumil.branchchatapp.api.RetrofitClient
import com.soumil.branchchatapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val username = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            performLogIn(username, password)
        }
    }

    private fun performLogIn(username: String, password: String) {
        val request = LoginRequest(username, password)

        if (username.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Error! Email or password field cannot empty.", Toast.LENGTH_LONG).show()
        }

        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(p0: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val token = response.body()?.auth_token
                    Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                    if (token != null){
                        saveAuthToken(token)

                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        i.putExtra("auth_token", token)
                        startActivity(i)
                        finish()
                    }
                }
                else{
                    Toast.makeText(this@LoginActivity, "Email or password is incorrect", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error! ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun saveAuthToken(token: String) {
        val sp: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }
}
