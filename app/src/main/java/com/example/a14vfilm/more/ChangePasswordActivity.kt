package com.example.a14vfilm.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChangePasswordActivity : AppCompatActivity() {
    var ETOld: EditText? = null
    var ETNew: EditText? = null
    var ETNew2: EditText? = null
    var BTNSubmit: Button? = null
    var IBBack: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        ETOld = findViewById(R.id.ETOldPass)
        ETNew = findViewById(R.id.ETNewPass)
        ETNew2 = findViewById(R.id.ETNewPass2)
        BTNSubmit = findViewById(R.id.BTNChangePass)
        IBBack = findViewById(R.id.IBCPBack)

        BTNSubmit!!.setOnClickListener {
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference("user")
            val query = ref.orderByChild("id").equalTo(UserLogin.info!!.id)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        val password = singleSnapshot.child("password").getValue<String>()
                        val result = BCrypt.verifyer().verify(ETOld!!.text.toString().toCharArray(), password)
                        if (result.verified) {
                            if (ETNew!!.text.toString().contains("[0-9]".toRegex()) && ETNew!!.text.toString().length >= 8) {
                                if (ETNew!!.text.toString() == ETNew2!!.text.toString()) {
                                    val hash = BCrypt.withDefaults().hashToString(10, ETNew!!.text.toString().toCharArray())
                                    ref.child(UserLogin.info!!.id).child("password").setValue(hash.toString())
                                    Toast.makeText(this@ChangePasswordActivity, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else
                                    Toast.makeText(this@ChangePasswordActivity, "Nhập lại mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show()
                            }
                            else
                                Toast.makeText(this@ChangePasswordActivity, "Mật khẩu mới không đạt yêu cầu", Toast.LENGTH_SHORT).show()
                        }
                        else
                            Toast.makeText(this@ChangePasswordActivity, "Sai mật khẩu cũ", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        IBBack!!.setOnClickListener {
            finish()
        }


        supportActionBar!!.hide()
    }
}