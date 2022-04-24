package com.example.a14vfilm.more

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.a14vfilm.R
import com.example.a14vfilm.models.UserLogin
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
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
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
            else {
                ref.child(UserLogin.info!!.id).child("name").setValue(ETName!!.text.toString())
                ref.child(UserLogin.info!!.id).child("address").setValue(ETAddress!!.text.toString())
                ref.child(UserLogin.info!!.id).child("phone").setValue(ETPhone!!.text.toString())
                finish()
            }
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
                        Picasso.get().load(imageUri).resize(150, 150).into(IVAvatar)
                        BTNSubmit!!.isClickable = true
                        Toast.makeText(this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show()
                    }
                }.addOnProgressListener {
                    Toast.makeText(this, "Đang cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show()
                    BTNSubmit!!.isClickable = false
                }
            }
        }
    }
}