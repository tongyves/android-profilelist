package com.example.listuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.listuser.Models.PostList
import com.example.listuser.Models.ProfileLists
import com.example.listuser.adapter.UserPostAdapter
import org.json.JSONArray
import org.json.JSONObject

class UserPost : AppCompatActivity() {

    private lateinit var rvPost : RecyclerView
    private lateinit var btnBack : ImageView
    private lateinit var imgProfile : ImageView
    private lateinit var tvName : TextView
    private lateinit var tvTotalPost : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)

        val id = intent.getStringExtra("id").toString()
        rvPost = findViewById(R.id.rvPost)
        imgProfile = findViewById(R.id.img_Profile)
        tvName = findViewById(R.id.tv_Name)
        tvTotalPost = findViewById(R.id.tvTotal)
        btnBack = findViewById(R.id.btn_back)

        getPost(id)
        getUserDetail(id)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getPost(id: String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://gorest.co.in/public/v1/users/$id/posts"
        var jsonData : JSONObject
        var jsonArray : JSONArray
        val data = ArrayList<PostList>()

        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            jsonArray = response.getJSONArray("data")
            val dataLength = jsonArray.length()
            tvTotalPost.text = "Total post: $dataLength"
            for (i in 0 until dataLength){
                jsonData = jsonArray.getJSONObject(i)
                val postData = PostList("","")
                postData.title = jsonData.getString("title").toString()
                postData.deScrip = jsonData.getString("body").toString()
                data.add(postData)
            }
            rvPost.apply {
                adapter = UserPostAdapter(data)
                layoutManager = LinearLayoutManager(this@UserPost)
            }
        },{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        })
        queue.add(stringRequest)

    }

    private fun getUserDetail(id: String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://gorest.co.in/public/v1/users/$id"
        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{
             response ->
            val jsonObject = response.getJSONObject("data")
            val profileData = ProfileLists("", "", "", "","")
            if(jsonObject.getString("gender") == "male"){
                Glide.with(this).load(profileData.imgMaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
            }else{
                Glide.with(this).load(profileData.imgFemaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
            }
            tvName.text = jsonObject.getString("name")
            if(tvTotalPost.text == "Total post: 0"){
                Toast.makeText(this, "${jsonObject.getString("name")} has no post.", Toast.LENGTH_SHORT).show()
            }
        },{
            
        })
        queue.add(stringRequest)
    }
}