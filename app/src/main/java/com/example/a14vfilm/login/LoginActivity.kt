package com.example.a14vfilm.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.example.a14vfilm.signup.SignupActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private var googleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null
    var BTNLogin: Button? = null
    var BTNSignup: Button? = null
    var BTNGoogle: Button? = null
    var ETEmail: EditText? = null
    var ETPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        BTNLogin = findViewById(R.id.BTNLoginForm)
        BTNSignup = findViewById(R.id.BTNSignup)
        BTNGoogle = findViewById(R.id.BTNGoogle)
        ETEmail = findViewById(R.id.ETEmail)
        ETPassword = findViewById(R.id.ETPassword)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("593119782088-hjurthm7o7lm2490vpj2imt0lt8jnpfg.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth= FirebaseAuth.getInstance()

        BTNSignup!!.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        BTNLogin!!.setOnClickListener {
            when {
                TextUtils.isEmpty(ETEmail!!.text.toString()) -> Toast.makeText(this, "Email không thể bỏ trống", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(ETPassword!!.text.toString()) -> Toast.makeText(this, "Mật khẩu không thể bỏ trống", Toast.LENGTH_SHORT).show()
                !isEmailValid(ETEmail!!.text.toString()) -> Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                else -> {
                    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                    val ref = FirebaseDatabase.getInstance(url).getReference("user")
                    val query = ref.orderByChild("email").equalTo(ETEmail!!.text.toString())
                    query.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists())
                                Toast.makeText(this@LoginActivity, "Email không tồn tại", Toast.LENGTH_SHORT).show()
                            for (singleSnapshot in snapshot.children) {
                                val id = singleSnapshot.child("id").getValue<String>()
                                val email = singleSnapshot.child("email").getValue<String>()
                                val password = singleSnapshot.child("password").getValue<String>()
                                val name = singleSnapshot.child("name").getValue<String>()
                                val address = singleSnapshot.child("address").getValue<String>()
                                val phone = singleSnapshot.child("phone").getValue<String>()
                                val image = singleSnapshot.child("image").getValue<String>()
                                val status = singleSnapshot.child("status").getValue<Boolean>()
                                val role = singleSnapshot.child("role").getValue<Int>()
                                val result = BCrypt.verifyer().verify(ETPassword!!.text.toString().toCharArray(), password)
                                if (result.verified) {
                                    if (status!!) {
                                        UserLogin.info = User(
                                            id!!,
                                            email!!,
                                            "password",
                                            name!!,
                                            address!!,
                                            phone!!,
                                            image!!,
                                            status,
                                            role!!
                                        )
                                        val gson = Gson()
                                        val json = gson.toJson(UserLogin.info)
                                        val sharedPreference =  getSharedPreferences("UserLogin",
                                            Context.MODE_PRIVATE)
                                        val editor = sharedPreference.edit()
                                        editor.putString("user", json)
                                        editor.commit()
                                        finish()
                                    }
                                    else
                                        Toast.makeText(this@LoginActivity, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show()
                                }
                                else
                                    Toast.makeText(this@LoginActivity, "Email hoặc mật khẩu sai", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
        }
        BTNGoogle!!.setOnClickListener {
            signIn()
        }

    }
    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 120)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken!!)

                } catch (e: ApiException) {}
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                    val currentUser = mAuth!!.currentUser
                    val ref = FirebaseDatabase.getInstance(url).getReference("user")
                    val query = ref.orderByChild("id").equalTo(currentUser!!.uid)
                    query.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                val temp = User(
                                    currentUser!!.uid,
                                    currentUser.email!!,
                                    "",
                                    currentUser.displayName!!,
                                    "",
                                    "",
                                    currentUser.photoUrl.toString(),
                                    true,
                                    0
                                )
                                ref.child(currentUser.uid).setValue(temp)
                            }
                            else {
                                for (singleSnapshot in snapshot.children) {
                                    val id = singleSnapshot.child("id").getValue<String>()
                                    val email = singleSnapshot.child("email").getValue<String>()
                                    val name = singleSnapshot.child("name").getValue<String>()
                                    val address = singleSnapshot.child("address").getValue<String>()
                                    val phone = singleSnapshot.child("phone").getValue<String>()
                                    val image = singleSnapshot.child("image").getValue<String>()
                                    val status = singleSnapshot.child("status").getValue<Boolean>()
                                    if (status!!) {
                                        UserLogin.info = User(
                                            id!!,
                                            email!!,
                                            "",
                                            name!!,
                                            address!!,
                                            phone!!,
                                            image!!,
                                            status,
                                            0
                                        )
                                        finish()
                                    }
                                    else {
                                        mAuth!!.signOut()
                                        Toast.makeText(this@LoginActivity, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
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