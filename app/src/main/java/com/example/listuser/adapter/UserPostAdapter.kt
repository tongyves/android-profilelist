package com.example.listuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listuser.Models.PostList
import com.example.listuser.R

class UserPostAdapter(
    private val postList: List<PostList>
) : RecyclerView.Adapter<UserPostAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_post_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserPostAdapter.ViewHolder, position: Int) {
                val list = postList[position]
                holder.tvTitle.text = "Title: ${list.title}"
                holder.tvDescript.text = list.deScrip
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        var tvDescript : TextView = itemView.findViewById(R.id.tv_Descript)
    }
}