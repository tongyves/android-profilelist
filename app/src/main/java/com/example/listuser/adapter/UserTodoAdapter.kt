package com.example.listuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.listuser.Models.TodoList
import com.example.listuser.R
import org.w3c.dom.Text

class UserTodoAdapter (
    private val todoList : List<TodoList>
        ):RecyclerView.Adapter<UserTodoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTodoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_todo_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserTodoAdapter.ViewHolder, position: Int) {
        val list = todoList[position]
        holder.tvTitle.text = "Title: ${list.title}"
        holder.tvStatus.text = "Status: ${list.status}"
        holder.tvDate.text = "Due on: ${list.date}"
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
        var tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvStatus : TextView = itemView.findViewById(R.id.tvStatus)
        val tvDate : TextView = itemView.findViewById(R.id.tvDate)
        val imgStatus : ImageView = itemView.findViewById(R.id.imgStatus)
    }

}