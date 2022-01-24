package com.example.listuser.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.listuser.Models.TodoList
import com.example.listuser.R
import org.json.JSONObject

class UserTodoAdapter (
    private val todoList : List<TodoList>
        ):RecyclerView.Adapter<UserTodoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTodoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_todo_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserTodoAdapter.ViewHolder, position: Int) {
        val list = todoList[position]
        holder.todoId = list.id
        holder.tvTitle.text = "Title:       ${list.title}"
        holder.tvStatus.text = "Status:   ${list.status}"
        holder.tvDate.text = "Due on:    ${list.date}"
        if(list.status == "pending"){
            holder.imgStatus.setImageResource(R.drawable.ic_time_left)
        }else{
            holder.imgStatus.setImageResource(R.drawable.ic_check_mark)
        }


    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvStatus : TextView = itemView.findViewById(R.id.tvStatus)!!
        val tvDate : TextView = itemView.findViewById(R.id.tvDate)
        val imgStatus : ImageView = itemView.findViewById(R.id.imgStatus)
        var todoId: String? = null
        init {
            itemView.setOnClickListener {
                updateTodoStatus()
            }
        }

        private fun updateTodoStatus(){
            val view = View.inflate(itemView.context,R.layout.cus_dialog_todostatus,null)
            val builder = AlertDialog.Builder(itemView.context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
            //dialog get info
            val tvCusTitle: TextView = dialog.findViewById(R.id.tv_cusTitle)
            val btnTodoStatus: Button = dialog.findViewById(R.id.btnTodoStatus)
            tvCusTitle.text = tvTitle.text.toString().replace("Title:       ","")
            val todoStatus = tvStatus.text.toString().replace("Status:   ","")
            if(todoStatus == "pending"){
                btnTodoStatus.text = "Completed"
            }else{
                btnTodoStatus.text = "Pending"
            }

            btnTodoStatus.setOnClickListener {
                val queue = Volley.newRequestQueue(dialog.context)
                val url ="https://gorest.co.in/public/v1/todos/$todoId"
                var jsonRequest = JSONObject()
                when(todoStatus){
                    "pending"-> jsonRequest.put("status","completed")
                    else ->     jsonRequest.put("status","pending")
                }

                val stringRequest = object : JsonObjectRequest(Method.PUT,url,jsonRequest,{ response ->
                    val jsonObject = response.getJSONObject("data")
                    val todoStatus = jsonObject.getString("status")
                    tvStatus.text = "Status:   $todoStatus"
                    when(todoStatus){
                        "pending"->{
                            tvStatus.text = "Status:   $todoStatus"
                            imgStatus.setImageResource(R.drawable.ic_time_left)
                        }
                        else->{
                            tvStatus.text = "Status:   $todoStatus"
                            imgStatus.setImageResource(R.drawable.ic_check_mark)
                        }

                    }

                    Toast.makeText(dialog.context, "Update Success", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                },{
                    Toast.makeText(dialog.context, "Change Failed", Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val token = "755ee9afdf138797d9e7bcb1aa644e8e136d881b9a94bf46f4e9acc8cb898681"
                        val headers = HashMap<String,String>()
                        headers["Authorization"] = "Bearer $token"
                        return headers
                    }
                }
                queue.add(stringRequest)


            }

        }
    }


}