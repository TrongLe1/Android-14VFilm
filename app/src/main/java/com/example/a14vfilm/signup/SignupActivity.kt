package com.example.a14vfilm.signup

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    var BTNSignup: Button? = null
    var ETEmail: EditText? = null
    var ETPassword: EditText? = null
    var ETPassword2: EditText? = null
    var ETName: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar!!.hide()

        ETEmail = findViewById(R.id.ETEmailSignup)
        ETPassword = findViewById(R.id.ETPasswordSignup)
        ETPassword2 = findViewById(R.id.ETRepassword)
        ETName = findViewById(R.id.ETNameSignup)
        BTNSignup = findViewById(R.id.BTNSignupDone)

        BTNSignup!!.setOnClickListener {
            when {
                TextUtils.isEmpty(ETEmail!!.text.toString()) -> Toast.makeText(this, "Email không thể bỏ trống", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(ETPassword!!.text.toString()) -> Toast.makeText(this, "Mật khẩu không thể bỏ trống", Toast.LENGTH_SHORT).show()
                !ETPassword!!.text.toString().contains("[0-9]".toRegex()) || ETPassword!!.text.toString().length < 8 -> Toast.makeText(this, "Mật khẩu không đạt yêu cầu", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(ETPassword2!!.text.toString()) -> Toast.makeText(this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(ETName!!.text.toString()) -> Toast.makeText(this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show()
                !isEmailValid(ETEmail!!.text.toString()) -> Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                ETPassword!!.text.toString() != ETPassword2!!.text.toString() -> Toast.makeText(this, "Mật khẩu nhập lại sai", Toast.LENGTH_SHORT).show()
                else -> {
                    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                    val ref = FirebaseDatabase.getInstance(url).getReference("user")
                    val query = ref.orderByChild("email").equalTo(ETEmail!!.text.toString())
                    query.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists())
                                Toast.makeText(this@SignupActivity, "Email đã được sử dụng", Toast.LENGTH_SHORT).show()
                            else {
                                val hash = BCrypt.withDefaults().hashToString(10, ETPassword!!.text.toString().toCharArray())
                                val user = User(ref.push().key!!, ETEmail!!.text.toString(),
                                    hash.toString(), ETName!!.text.toString(), "", "", "", true, 0)
                                ref.child(user.id).setValue(user)
                                finish()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
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