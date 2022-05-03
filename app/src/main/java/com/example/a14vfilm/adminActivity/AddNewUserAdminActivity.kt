package com.example.a14vfilm.adminActivity

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import java.util.regex.Pattern

class AddNewUserAdminActivity : AppCompatActivity() {

    private var etUserName: EditText? = null
    private var etUserEmail: EditText? = null
    private var etUserPassword: EditText? = null
    private var etUserAddress: EditText? = null
    private var etUserPhone: EditText? = null
    private var ivAvatar: ImageView? = null
    private var spinnerRole: Spinner? = null

    var imageUri: Uri? = null
    var userImage: String = ""
    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    val ref = FirebaseDatabase.getInstance(url).getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user_admin)
        supportActionBar!!.hide()

        etUserName = findViewById(R.id.addnewuseradminactivity_etUserName)
        etUserEmail = findViewById(R.id.addnewuseradminactivity_etUserEmail)
        etUserPassword = findViewById(R.id.addnewuseradminactivity_etUserPassword)
        etUserAddress = findViewById(R.id.addnewuseradminactivity_etUserAddress)
        etUserPhone = findViewById(R.id.addnewuseradminactivity_etUserPhone)
        ivAvatar = findViewById(R.id.addnewuseradminactivity_IVDIAvatar)
        spinnerRole = findViewById(R.id.addnewuseradminactivity_spinner)
        //spinner choose role
        var list_of_roles = arrayOf("Nguời mua", "Người bán", "Quản trị viên")
        val array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_roles)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole!!.adapter = array_adapter


        findViewById<Button>(R.id.addnewuseradminactivity_addUser).setOnClickListener{

            when {
                TextUtils.isEmpty(etUserEmail!!.text.toString()) -> Toast.makeText(this, "Email không thể bỏ trống", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(etUserPassword!!.text.toString()) -> Toast.makeText(this, "Mật khẩu không thể bỏ trống", Toast.LENGTH_SHORT).show()
                !etUserPassword!!.text.toString().contains("[0-9]".toRegex()) || etUserPassword!!.text.toString().length < 8 -> Toast.makeText(this, "Mật khẩu không đạt yêu cầu", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(etUserName!!.text.toString()) -> Toast.makeText(this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show()
                !isEmailValid(etUserEmail!!.text.toString()) -> Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                else -> {
                    var check = spinnerRole!!.selectedItem.toString()
                    var userRole:Int? = null

                    if(check == "Nguời mua"){
                        userRole = 0
                    }
                    else if(check == "Nguời bán"){
                        userRole = 1
                    }
                    else {
                        userRole = 2
                    }

                    val query = ref.orderByChild("email").equalTo(etUserEmail!!.text.toString())
                    query.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists())
                                Toast.makeText(it.context, "Email đã được sử dụng", Toast.LENGTH_SHORT).show()
                            else {
                                val hash = BCrypt.withDefaults().hashToString(10, etUserPassword!!.text.toString().toCharArray())
                                val user = User(ref.push().key!!, etUserEmail!!.text.toString(),
                                    hash.toString(), etUserName!!.text.toString(), etUserAddress!!.text.toString(), etUserPhone!!.text.toString(), userImage.toString(), true, userRole)
                                ref.child(user.id).setValue(user)
                                Toast.makeText(it.context, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show()

                                finish()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })

                }
            }

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

    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email!!)
        return matcher.matches()
    }
}