package com.example.listuser.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.listuser.MainActivity
import com.example.listuser.R
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.io.StringReader
import java.net.URL

class Login : AppCompatActivity() {

    private lateinit var tvSignUp : TextView
    private lateinit var btnLogin : Button
    private lateinit var etEmail : EditText
    private lateinit var etPassword: EditText
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)
        tvSignUp = findViewById(R.id.tvSignUp)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
            when{
                etEmail.text.isEmpty() -> Toast.makeText(this,"Email is empty",Toast.LENGTH_SHORT).show()
                etPassword.text.isEmpty() -> Toast.makeText(this,"Password is empty",Toast.LENGTH_SHORT).show()
                else -> {
                    stringRequest()
                }
            }
        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun stringRequest(){
        val url = "https://reqres.in/api/login"
        val jsonObject = JSONObject()
        val queue = Volley.newRequestQueue(this)
        jsonObject.put("username",etEmail.text.toString().trim())
        jsonObject.put("password",etPassword.text.toString())

        val stringRequest = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            {
                response -> Toast.makeText(this,"$response",Toast.LENGTH_SHORT).show()
                token = response.toString()
                intent()
            },{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            })
        queue.add(stringRequest)
    }

    private fun intent(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}