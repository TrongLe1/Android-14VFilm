package com.example.a14vfilm.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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

class LoginActivity : AppCompatActivity() {
    private var googleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null
    var BTNLogin: Button? = null
    var BTNSignup: Button? = null
    var BTNGoogle: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        BTNLogin = findViewById(R.id.BTNLoginForm)
        BTNSignup = findViewById(R.id.BTNSignup)
        BTNGoogle = findViewById(R.id.BTNGoogle)

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
                    query.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                val temp = User(
                                    currentUser!!.uid,
                                    currentUser.email!!,
                                    "",
                                    currentUser.displayName!!,
                                    "",
                                    "",
                                    currentUser.photoUrl.toString()
                                )
                                ref.child(currentUser.uid).setValue(temp)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                    UserLogin.info = User(
                        currentUser!!.uid,
                        currentUser.email!!,
                        "",
                        currentUser.displayName!!,
                        "",
                        "",
                        currentUser.photoUrl.toString()
                    )
                    finish()
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
    }
}