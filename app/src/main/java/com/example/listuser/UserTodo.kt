package com.example.listuser

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.listuser.Models.ProfileLists
import com.example.listuser.Models.TodoList
import com.example.listuser.adapter.UserTodoAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserTodo : AppCompatActivity() {
    private lateinit var rvtodo : RecyclerView
    private lateinit var btnBack : ImageView
    private lateinit var imgProfile : ImageView
    private lateinit var tvName : TextView
    private lateinit var tvTotalTodo : TextView
    private lateinit var btnCreate : ExtendedFloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_todo)

        val id = intent.getStringExtra("id").toString()

        rvtodo = findViewById(R.id.rvTodo)
        btnBack = findViewById(R.id.btn_back)
        imgProfile = findViewById(R.id.img_Profile)
        tvName = findViewById(R.id.tv_Name)
        tvTotalTodo = findViewById(R.id.tvTotalTodo)
        btnCreate = findViewById(R.id.btnCreateTodo)
        getUserDetail(id)
        getTodo(id)
        btnBack.setOnClickListener {
            finish()
        }
        btnCreate.setOnClickListener {
            createTodo(id)
        }


    }

    private fun createTodo(id: String) {
        //Dialog
        val view = View.inflate(this,R.layout.cus_dialog_todocreate,null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val etTodoTitle : TextInputEditText = view.findViewById(R.id.etTodoTitle)
        val acStatus : AutoCompleteTextView = view.findViewById(R.id.acStatus)
        val etDate : TextInputEditText = view.findViewById(R.id.etDate)

        //drop down
        acStatus.inputType = InputType.TYPE_NULL

        val status = listOf("completed","pending")
        val adapter = ArrayAdapter(this,R.layout.dropdown_todo_status,status)
        acStatus.setAdapter(adapter)

        //datePickerDialog
        etDate.inputType = InputType.TYPE_NULL
        etDate.isFocusable = false
        val calender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayofmonth ->
            calender.apply {
                set(Calendar.YEAR,year)
                set(Calendar.MONTH,month)
                set(Calendar.DAY_OF_MONTH,dayofmonth)
            }
            val myFormat = "MMM dd,yyyy"
            val formatter = SimpleDateFormat(myFormat, Locale.ENGLISH)
            etDate.setText(formatter.format(calender.time))
        }
        etDate.setOnClickListener{
            DatePickerDialog(this,datePicker,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)).show()

        }

        //Button Listener
        val btnCreate : Button = view.findViewById(R.id.btnDialogCreate)
        val btnCancel : TextView = view.findViewById(R.id.btnDialogCancel)

        btnCreate.setOnClickListener {
            if (etTodoTitle.text == null && acStatus.text == null){

            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

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
            val outputDateFormat = SimpleDateFormat("MMM DD,yyyy", Locale.ENGLISH)
            val inputDateFormat = SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.sss+05:30", Locale.ENGLISH)
            for (i in 0 until dataLength){
                jsonObject = jsonArray.getJSONObject(i)
                val todoData = TodoList("","","","")
                todoData.id = jsonObject.getString("id")
                todoData.title = jsonObject.getString("title")
                todoData.status = jsonObject.getString("status").toString()
               //date format
                val date = inputDateFormat.parse(jsonObject.getString("due_on"))
                val myDateFormat = outputDateFormat.format(date)
                todoData.date = myDateFormat
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

    private fun getUserDetail(id: String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://gorest.co.in/public/v1/users/$id"
        val userList = ProfileLists("","","","","")
        var jsonObject : JSONObject
        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            jsonObject = response.getJSONObject("data")
            tvName.text = jsonObject.getString("name")
            if(jsonObject.getString("gender") == "male"){
                Glide.with(this).load(userList.imgMaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
            }else{
                Glide.with(this).load(userList.imgFemaleUrl).centerCrop().into(imgProfile)
            }

        },{

        })
        queue.add(stringRequest)

    }
}