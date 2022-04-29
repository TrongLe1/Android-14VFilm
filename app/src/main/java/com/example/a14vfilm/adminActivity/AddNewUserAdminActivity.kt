package com.example.a14vfilm.adminActivity

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class AddNewUserAdminActivity : AppCompatActivity() {

    private var etUserName: EditText? = null
    private var etUserEmail: EditText? = null
    private var etUserPassword: EditText? = null
    private var etUserAddress: EditText? = null
    private var etUserPhone: EditText? = null
    private var ivAvatar: ImageView? = null
    var imageUri: Uri? = null
    var userImage: String = ""
    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    val ref = FirebaseDatabase.getInstance(url).getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user_admin)

        etUserName = findViewById(R.id.addnewuseradminactivity_etUserName)
        etUserEmail = findViewById(R.id.addnewuseradminactivity_etUserEmail)
        etUserPassword = findViewById(R.id.addnewuseradminactivity_etUserPassword)
        etUserAddress = findViewById(R.id.addnewuseradminactivity_etUserAddress)
        etUserPhone = findViewById(R.id.addnewuseradminactivity_etUserPhone)
        ivAvatar = findViewById(R.id.addnewuseradminactivity_IVDIAvatar)


        findViewById<Button>(R.id.addnewuseradminactivity_addUser).setOnClickListener{
            var userName = etUserName!!.text.toString()
            var userEmail = etUserEmail!!.text.toString()
            var userPassword = etUserPassword!!.text.toString()
            var userAddress = etUserAddress!!.text.toString()
            var userPhone = etUserPhone!!.text.toString()


            val user = User(ref.push().key!!, userEmail, userPassword,userName, userAddress, userPhone, userImage!!,true)
            ref.child(user.id).setValue(user)
            Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ViewUserActivity::class.java)
//            startActivity(intent)
            finish()
        }

        ivAvatar!!.setOnClickListener {
            selectImage()
        }

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
                        userImage = it.toString()

                        Picasso.get().load(imageUri).resize(150, 150).into (ivAvatar)
                    }
                }.addOnProgressListener {
                }
            }
        }
    }
}