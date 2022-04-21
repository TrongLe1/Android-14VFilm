package com.example.a14vfilm.adminActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.google.firebase.database.FirebaseDatabase

class AddNewUserAdminActivity : AppCompatActivity() {

    private var etUserName: EditText? = null
    private var etUserEmail: EditText? = null
    private var etUserPassword: EditText? = null
    private var etUserAddress: EditText? = null
    private var etUserPhone: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user_admin)

        etUserName = findViewById(R.id.addnewuseradminactivity_etUserName)
        etUserEmail = findViewById(R.id.addnewuseradminactivity_etUserEmail)
        etUserPassword = findViewById(R.id.addnewuseradminactivity_etUserPassword)
        etUserAddress = findViewById(R.id.addnewuseradminactivity_etUserAddress)
        etUserPhone = findViewById(R.id.addnewuseradminactivity_etUserPhone)


        findViewById<Button>(R.id.addnewuseradminactivity_addUser).setOnClickListener{
            var userName = etUserName!!.text.toString()
            var userEmail = etUserEmail!!.text.toString()
            var userPassword = etUserPassword!!.text.toString()
            var userAddress = etUserAddress!!.text.toString()
            var userPhone = etUserAddress!!.text.toString()

            val url = "https://demohw-9a24d-default-rtdb.firebaseio.com/"
            val ref = FirebaseDatabase.getInstance(url).getReference("user")
            val user = User(ref.push().key!!,userName, userEmail, userPassword, userAddress, userPhone, "On")
            ref.child(user.id).setValue(user)
            Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ViewUserActivity::class.java)
            startActivity(intent)
        }

    }
}