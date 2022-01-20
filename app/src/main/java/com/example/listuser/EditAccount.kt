package com.example.listuser

import android.app.VoiceInteractor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.listuser.Models.ProfileLists
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

class EditAccount : AppCompatActivity() {
    private lateinit var btnBack : ImageView
    private lateinit var tvEditAcc : TextView
    private lateinit var tvName : TextView
    private lateinit var imgProfile : ImageView

    private lateinit var etUsername : EditText
    private lateinit var etStatus : EditText
    private lateinit var lyStatus : TextInputLayout
    private lateinit var etEmail: EditText
    private lateinit var etGender: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)
        val id = intent.getStringExtra("id").toString()

        btnBack = findViewById(R.id.btn_back)
        tvEditAcc = findViewById(R.id.tv_EditAcc)
        tvName = findViewById(R.id.tv_Name)
        imgProfile = findViewById(R.id.img_Profile)

        etUsername = findViewById(R.id.et_Username)
        etStatus = findViewById(R.id.et_Status)
        lyStatus = findViewById(R.id.ly_status)
        etEmail = findViewById(R.id.et_Email)
        etGender = findViewById(R.id.et_Gender)

        getProfileList(id)

        btnBack.setOnClickListener {
            finish()
        }

        tvEditAcc.setOnClickListener {
            editAcc(id)
        }

        etUsername.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                tvEditAcc.apply {
                    isEnabled = true
                    setTextColor(ContextCompat.getColor(context,R.color.cus_pink))
                }
            }

        })

        etStatus.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                tvEditAcc.apply {
                    isEnabled = true
                    setTextColor(ContextCompat.getColor(context,R.color.cus_pink))
                }
            }

        })

    }

    private fun update(id: String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://gorest.co.in/public/v1/users/$id"
        val token = "755ee9afdf138797d9e7bcb1aa644e8e136d881b9a94bf46f4e9acc8cb898681"
        var jsonRequest = JSONObject()
        jsonRequest.put("name",etUsername.text.toString())
        jsonRequest.put("status",etStatus.text.toString())
        val stringRequest = object: JsonObjectRequest(Method.PUT,url,jsonRequest,{
                response ->
            Log.i("Update Response",response.toString())
            tvEditAcc.apply {
                text = "Edit"
                setTextColor(ContextCompat.getColor(context,R.color.black))
            }

        },{
            Toast.makeText(this@EditAccount, "failed", Toast.LENGTH_SHORT).show()
        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun editAcc(id :String){
        if(tvEditAcc.text == "Edit"){
            Toast.makeText(this, "You can edit Username and Status", Toast.LENGTH_SHORT).show()

            tvEditAcc.apply{
                isEnabled = false
                text = "Save"
                setTextColor(resources.getColor(R.color.cus_grey))

            }
            etUsername.apply {
                isFocusableInTouchMode = true
                requestFocus(etUsername.text.length)
            }

            etStatus.isFocusableInTouchMode = true
            lyStatus.hintTextColor = resources.getColorStateList(R.color.cus_pink)


        }else if(tvEditAcc.text == "Save"){
            Toast.makeText(this, "Save clicked", Toast.LENGTH_SHORT).show()
            update(id)

            etUsername.apply {
                isFocusableInTouchMode = false
                clearFocus()
            }
            etStatus.apply {
                isFocusableInTouchMode = false
                clearFocus()
            }
        }
    }

    private fun getProfileList(id: String){
        val queue = Volley.newRequestQueue(this)
        val url = "https://gorest.co.in/public/v1/users/"
        var jsonArray : JSONArray
        var jsonObject: JSONObject
        var profile = ProfileLists("","","","","")
        val stringRequest = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            jsonArray = response.getJSONArray("data")

            for(i in 0 until jsonArray.length()){
                jsonObject = jsonArray.getJSONObject(i)
                if(jsonObject.getString("id") == id){
                    etUsername.setText(jsonObject.getString("name"))
                    tvName.text = jsonObject.getString("name")
                    etStatus.setText(jsonObject.getString("status"))
                    etEmail.setText(jsonObject.getString("email"))
                    etGender.setText(jsonObject.getString("gender"))
                    if (jsonObject.getString("gender") == "male"){
                        Glide.with(this).load(profile.imgMaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
                    }else{
                        Glide.with(this).load(profile.imgFemaleUrl).centerCrop().placeholder(R.drawable.origi).into(imgProfile)
                    }
                }
            }
        },{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        })
        queue.add(stringRequest)
    }




}