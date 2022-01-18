package com.example.listuser.Models

class ProfileList {
    private var name: String? = null
    private var gender: String? = null
    private var email: String? = null
    private var status: String? = null


    init {
        this.name = name!!
        this.gender = gender!!
        this.email = email!!
        this.status = status!!

    }
    fun getName() : String?{
        return name
    }
    fun setName(name : String?){
        this.name = name!!
    }
}