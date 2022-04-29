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
    private var tvUserPassword: TextView? = null
    private var tvUserAddress: TextView? = null
    private var tvUserPhone: TextView? = null
    private var ivUserAvatar: CircleImageView? = null


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
        val userAvatar:String = intent.getStringExtra("userImage").toString()

        //set layout with user information
        tvUserName!!.text = userName
        tvUserEmail!!.text = userEmail
        tvUserPassword!!.text = userPassword
        tvUserAddress!!.text = userAddress
        tvUserPhone!!.text = userPhone

        if (userAvatar != "")
            Picasso.get().load(userAvatar).resize(150, 150).into(ivUserAvatar)


//        findViewById<Button>(R.id.viewuserdetail_deleteUser).setOnClickListener{
//            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
//            val ref = FirebaseDatabase.getInstance(url).getReference()
//            val query = ref.child("user").child(userID)
//            query.removeValue()
//            Toast.makeText(it.context, "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show()
//            //send mail to user
//            val intent1 = Intent(Intent.ACTION_SENDTO)
//            intent1.putExtra(Intent.EXTRA_SUBJECT, "Tài khoản 14VFilm của bạn đã bị xóa");
//            intent1.putExtra(Intent.EXTRA_TEXT, "Tài khoản của bạn đã bị xóa vì vi phạm chính sách người dùng");
//            intent1.setData(Uri.parse("mailto:"+userEmail));
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//            startActivity(intent1)
////
////            val intent2 = Intent(it.context, ViewUserActivity::class.java)
////            startActivity(intent2)
//
//        }
    }

    fun initComponent(){
        tvUserName = findViewById(R.id.viewuserdetail_tvUserName)
        tvUserEmail = findViewById(R.id.viewuserdetail_tvUserEmail)
        tvUserPassword = findViewById(R.id.viewuserdetail_tvUserPassword)
        tvUserAddress = findViewById(R.id.viewuserdetail_tvUserAddress)
        tvUserPhone = findViewById(R.id.viewuserdetail_tvUserPhone)
        ivUserAvatar = findViewById(R.id.viewuserdetail_imageView)
    }
}