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
import com.example.listuser.Models.TodoList
import com.example.listuser.adapter.UserTodoAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserTodo : AppCompatActivity() {
    private lateinit var rvtodo : RecyclerView
    private lateinit var btnBack : ImageView
    private lateinit var imgProfile : ImageView
    private lateinit var tvName : TextView
    private lateinit var tvTotalTodo : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_todo)

        val id = intent.getStringExtra("id").toString()

        rvtodo = findViewById(R.id.rvTodo)
        btnBack = findViewById(R.id.btn_back)
        imgProfile = findViewById(R.id.img_Profile)
        tvName = findViewById(R.id.tv_Name)
        tvTotalTodo = findViewById(R.id.tvTotalTodo)

        getTodo(id)
    }
    private fun getTodo(id : String){
        val queue = Volley.newRequestQueue(this)
        //id :16 has todos
        val url ="https://gorest.co.in/public/v1/users/$id/todos"
        var jsonObject: JSONObject
        var jsonArray: JSONArray
        val data = ArrayList<TodoList>()

        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            jsonArray = response.getJSONArray("data")
            val dataLength = jsonArray.length()
            tvTotalTodo.text = "Total Todos: $dataLength"

//            val outputDateFormat = SimpleDateFormat("MM-DD-yyyy", Locale.ENGLISH)
//            val inputDateFormat = SimpleDateFormat("yyyy-MM-DD'T'hh:mm:sssZD", Locale.ENGLISH)

            for (i in 0 until dataLength){
                jsonObject = jsonArray.getJSONObject(i)
                val todoData = TodoList("","","")
                todoData.title = jsonObject.getString("title")
                todoData.status = jsonObject.getString("status").toString()
                todoData.date = jsonObject.getString("due_on").toString()
//                val date = inputDateFormat.parse(jsonObject.getString("due_on").toString())
//                val myDateFormat = outputDateFormat.format(date)
//                todoData.date = myDateFormat
                data.add(todoData)
            }

            rvtodo.apply {
                adapter = UserTodoAdapter(data)
                layoutManager = LinearLayoutManager(this@UserTodo)
            }
        },{

        })
        queue.add(stringRequest)

    }
}