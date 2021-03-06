package com.example.a14vfilm.more

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.example.a14vfilm.R
import com.example.a14vfilm.models.UserLogin
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class InfoActivity : AppCompatActivity() {

    var ETEmail: EditText? = null
    var ETName: EditText? = null
    var ETAddress: EditText? = null
    var ETPhone: EditText? = null
    var BTNSubmit: Button? = null
    var IVAvatar: CircleImageView? = null
    var imageUri: Uri? = null
    var IBBack: ImageButton? = null
    var IVBackground: ImageView? = null
    private val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val ref = FirebaseDatabase.getInstance(url).getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        ETEmail = findViewById(R.id.ETDIEmail)
        ETName = findViewById(R.id.ETDIName)
        ETAddress = findViewById(R.id.ETDIAddress)
        ETPhone = findViewById(R.id.ETDIPhone)
        BTNSubmit = findViewById(R.id.BTNSubmit)
        IVAvatar = findViewById(R.id.IVDIAvatar)
        IBBack = findViewById(R.id.IBDIBack)
        IVBackground = findViewById(R.id.IVBackground)

        IVBackground!!.clipToOutline = true


        if (UserLogin.info!!.image != "")
            Picasso.get().load(UserLogin.info!!.image).resize(150, 150).into(IVAvatar)
        ETEmail!!.setText(UserLogin.info!!.email)
        ETName!!.setText(UserLogin.info!!.name)
        ETAddress!!.setText(UserLogin.info!!.address)
        ETPhone!!.setText(UserLogin.info!!.phone)

        IVAvatar!!.setOnClickListener {
            selectImage()
        }

        BTNSubmit!!.setOnClickListener {
            if (ETPhone!!.text.length != 10)
                Toast.makeText(this, "S??? ??i???n tho???i kh??ng h???p l???", Toast.LENGTH_SHORT).show()
            else if (TextUtils.isEmpty(ETName!!.text.toString()))
                Toast.makeText(this, "T??n ng?????i d??ng kh??ng ???????c ????? tr???ng", Toast.LENGTH_SHORT).show()
            else {
                ref.child(UserLogin.info!!.id).child("name").setValue(ETName!!.text.toString())
                ref.child(UserLogin.info!!.id).child("address").setValue(ETAddress!!.text.toString())
                ref.child(UserLogin.info!!.id).child("phone").setValue(ETPhone!!.text.toString())
                UserLogin.info!!.name = ETName!!.text.toString()
                UserLogin.info!!.address = ETAddress!!.text.toString()
                UserLogin.info!!.phone = ETPhone!!.text.toString()
                val gson = Gson()
                val json = gson.toJson(UserLogin.info)
                val sharedPreference =  getSharedPreferences("UserLogin",
                    Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("user", json)
                editor.commit()
                Toast.makeText(this, "C???p nh???p th??ng tin th??nh c??ng", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        IBBack!!.setOnClickListener {
            finish()
        }


        supportActionBar!!.hide()

    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            if (imageUri != null) {
                val fileName = UUID.randomUUID().toString() + ".jpg"
                val storageRef = FirebaseStorage.getInstance("gs://vfilm-83cf4.appspot.com").reference.child("avatars/$fileName")
                storageRef.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        val imageUrl = it.toString()
                        ref.child(UserLogin.info!!.id).child("image").setValue(imageUrl)
                        Picasso.get().load(imageUri).resize(150, 150).into  (IVAvatar)
                        BTNSubmit!!.isClickable = true
                        UserLogin.info!!.image = imageUrl
                        val gson = Gson()
                        val json = gson.toJson(UserLogin.info)
                        val sharedPreference =  getSharedPreferences("UserLogin",
                            Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putString("user", json)
                        editor.commit()
                        Toast.makeText(this, "C???p nh???t ???nh ?????i di???n th??nh c??ng", Toast.LENGTH_SHORT).show()
                    }
                }.addOnProgressListener {
                    //Toast.makeText(this, "??ang c???p nh???t ???nh ?????i di???n", Toast.LENGTH_SHORT).show()
                    //BTNSubmit!!.isClickable = false
                }
            }
        }
    }
}