package com.example.a14vfilm.adminActivity

import android.content.Intent
import android.net.Uri
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
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ViewUserDetailActivity : AppCompatActivity() {

    private var tvUserName: TextView? = null
    private var tvUserEmail: TextView? = null
    private var tvUserAddress: TextView? = null
    private var tvUserPhone: TextView? = null
    private var ivUserAvatar: CircleImageView? = null
    private var tvUserRole: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_detail)

        initComponent()
        supportActionBar!!.hide()


        //get data from intent
        val userID:String = intent.getStringExtra("userID").toString()
        val userName:String = intent.getStringExtra("userName").toString()
        val userEmail:String = intent.getStringExtra("userEmail").toString()
        val userAddress:String = intent.getStringExtra("userAddress").toString()
        val userPhone:String = intent.getStringExtra("userPhone").toString()
        val userAvatar:String = intent.getStringExtra("userImage").toString()
        val userRole:Int = intent.getIntExtra("userRole", 0)

        //set layout with user information
        tvUserName!!.text = userName
        tvUserEmail!!.text = userEmail
        tvUserAddress!!.text = userAddress
        tvUserPhone!!.text = userPhone

        if (userRole == 0){
            tvUserRole!!.text = "Ng?????i mua"
        }
        else if (userRole == 1){
            tvUserRole!!.text = "Ng?????i b??n"
        }
        else if (userRole == 2){
            tvUserRole!!.text = "Qu???n tr??? vi??n"
        }

        if (userAvatar != "")
            Picasso.get().load(userAvatar).resize(150, 150).into(ivUserAvatar)


    }

    fun initComponent(){
        tvUserName = findViewById(R.id.viewuserdetail_tvUserName)
        tvUserEmail = findViewById(R.id.viewuserdetail_tvUserEmail)
        tvUserAddress = findViewById(R.id.viewuserdetail_tvUserAddress)
        tvUserPhone = findViewById(R.id.viewuserdetail_tvUserPhone)
        ivUserAvatar = findViewById(R.id.viewuserdetail_imageView)
        tvUserRole = findViewById(R.id.viewuserdetail_tvUserRole)
    }
}