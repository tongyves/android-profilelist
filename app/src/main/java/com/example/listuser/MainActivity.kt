package com.example.listuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.listuser.Models.ProfileLists
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var rvProfile: RecyclerView
    private lateinit var tvTotal: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //RecycleView DynamicList
        rvProfile = findViewById(R.id.rvProfile)
        tvTotal = findViewById(R.id.tvTotal)
        profileList()
    }

    private fun profileList(){
        val data = ArrayList<ProfileLists>()

        val queue = Volley.newRequestQueue(this)
        var url ="https://gorest.co.in/public/v1/users?access-token=c9a937f6edee37fee3313a14efe69d629aa76d7c91138e1ee2a999685c7aadd3&page=1"
        var jsonArray: JSONArray
        var jsonData: JSONObject

        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{ response ->
            jsonArray = response.getJSONArray("data")
            val dataLength = jsonArray.length()
            tvTotal.text = "Total: $dataLength"

            for (i in 0 until dataLength){
                jsonData = jsonArray.getJSONObject(i)
                //profileName.add(jsonData.getString("name"))
                val profileData = ProfileLists("","","","","")
                profileData.id = jsonData.getString("id")
                profileData.name = jsonData.getString("name")
                profileData.email = jsonData.getString("email")
                profileData.gender = jsonData.getString("gender")
                profileData.status = jsonData.getString("status")
                data.add(profileData)
            }
        //val adapter = ProfileAdapter(data)
        rvProfile.apply {
            adapter = ProfileAdapter(context,data)
            layoutManager = GridLayoutManager(this@MainActivity,2)
        }
           // Log.i("Mainactivity","finish string req")
        },{
            Toast.makeText(this, "Error Loading", Toast.LENGTH_SHORT).show()
        })
        queue.add(stringRequest)
    }
}