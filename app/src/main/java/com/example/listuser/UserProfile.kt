package com.example.listuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.listuser.Models.ProfileLists
import org.json.JSONArray
import org.json.JSONObject

class UserProfile : AppCompatActivity() {

    private lateinit var btnBack : ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvStatus: TextView
    private lateinit var imgProfile: ImageView
    private lateinit var tvEditAcc : TextView
    private lateinit var tvTodo : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        btnBack = findViewById(R.id.btn_back)
        imgProfile = findViewById(R.id.img_Profile)
        tvName = findViewById(R.id.tv_Name)
        tvEmail = findViewById(R.id.tv_Email)
        tvStatus = findViewById(R.id.tv_Status)
        tvEditAcc= findViewById(R.id.tv_Edit)

        val id = intent.getStringExtra("id").toString()
        userList(id)
        //Listener
        btnBack.setOnClickListener {
            finish()
        }

        tvEditAcc.setOnClickListener{
            val intent = Intent(this,EditAccount::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }


    }

    private fun userList(id : String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://gorest.co.in/public/v1/users/"
        var jsonArray : JSONArray
        var jsonObject : JSONObject
        var profile = ProfileLists("","","","","")
        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            jsonArray = response.getJSONArray("data")
            for(i in 0 until jsonArray.length()){
                jsonObject = jsonArray.getJSONObject(i)
                if(jsonObject.getString("id") == id){
                    if(jsonObject.getString("gender") == "male"){
                        Glide.with(this).load(profile.imgMaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
                    }else{
                        Glide.with(this).load(profile.imgFemaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
                    }
                    tvName.text = jsonObject.getString("name")
                    tvEmail.text = jsonObject.getString("email")
                    if(jsonObject.getString("status") == "inactive"){
                        tvStatus.apply {
                            gravity = Gravity.CENTER
                            setText(R.string.status_inactive)
                            setTextColor(ContextCompat.getColor(context,R.color.cus_pink))
                            setBackgroundResource(R.drawable.btn_rounded_withstroke)
                        }
                    }
                }
            }

        },{
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
        })
        queue.add(stringRequest)


    }
}