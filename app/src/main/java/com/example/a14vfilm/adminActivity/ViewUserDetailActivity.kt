package com.example.a14vfilm.adminActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.a14vfilm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

class ViewUserDetailActivity : AppCompatActivity() {

    private var tvUserName: TextView? = null
    private var tvUserEmail: TextView? = null
    private var tvUserPassword: TextView? = null
    private var tvUserAddress: TextView? = null
    private var tvUserPhone: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_detail)

        initComponent()

        //get data from intent
        val userID:String = intent.getStringExtra("userID").toString()
        val userName:String = intent.getStringExtra("userName").toString()
        val userEmail:String = intent.getStringExtra("userEmail").toString()
        val userPassword:String = intent.getStringExtra("userPassword").toString()
        val userAddress:String = intent.getStringExtra("userAddress").toString()
        val userPhone:String = intent.getStringExtra("userPhone").toString()

        //set layout with user information
        tvUserName!!.text = userName
        tvUserEmail!!.text = userEmail
        tvUserPassword!!.text = userPassword
        tvUserAddress!!.text = userAddress
        tvUserPhone!!.text = userPhone

        findViewById<Button>(R.id.viewuserdetail_deleteUser).setOnClickListener{
            val url = "https://demohw-9a24d-default-rtdb.firebaseio.com/"
            val ref = FirebaseDatabase.getInstance(url).getReference()
            val query = ref.child("user").child(userID)
            query.removeValue()
            Toast.makeText(it.context, "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show()
            val intent = Intent(it.context, ViewUserActivity::class.java)
            startActivity(intent)
        }
    }

    fun initComponent(){
        tvUserName = findViewById(R.id.viewuserdetail_tvUserName)
        tvUserEmail = findViewById(R.id.viewuserdetail_tvUserEmail)
        tvUserPassword = findViewById(R.id.viewuserdetail_tvUserPassword)
        tvUserAddress = findViewById(R.id.viewuserdetail_tvUserAddress)
        tvUserPhone = findViewById(R.id.viewuserdetail_tvUserPhone)
    }
}