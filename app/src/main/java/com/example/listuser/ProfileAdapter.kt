package com.example.listuser

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listuser.Models.ProfileLists


class ProfileAdapter(
    private val context: Context,
    private val profileList: List<ProfileLists>
): RecyclerView.Adapter<ProfileAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.cardlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
        val profile = profileList[position]
        holder.id = profile.id
        holder.tvName.text = profile.name
        holder.tvEmail.text = profile.email
        if(profile.status == "inactive"){
            holder.tvStatus.apply {
                gravity = Gravity.CENTER
                setText(R.string.status_inactive)
                setTextColor(ContextCompat.getColor(context,R.color.cus_pink))
                setBackgroundResource(R.drawable.btn_rounded_withstroke)
            }
        }
        if(profile.gender == "male"){
            Glide.with(context).load(profile.imgMaleUrl).centerCrop().placeholder(R.drawable.origi).into(holder.imgProfile)
        }else{
            Glide.with(context).load(profile.imgFemaleUrl).centerCrop().placeholder(R.drawable.origi).into(holder.imgProfile)
        }
        //holder.bind(position)

    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var id :String? = null

        init{
          //  itemView.setOnClickListener(this)
            itemView.setOnClickListener{
               // val position: Int = adapterPosition
                Toast.makeText(itemView.context,"You select user ID:${id}",Toast.LENGTH_SHORT).show()

                val intent = Intent(itemView.context,UserProfile::class.java)
                intent.putExtra("id",id.toString())
                context.startActivity(intent)
            }
        }
    }

}