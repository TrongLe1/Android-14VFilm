package com.example.a14vfilm.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.a14vfilm.R
import com.example.a14vfilm.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    var BTNLogin: Button? = null
    var BTNSignup: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        BTNLogin = findViewById(R.id.BTNLoginForm)
        BTNSignup = findViewById(R.id.BTNSignup)
        BTNLogin!!.setBackgroundColor(Color.BLUE)
        BTNSignup!!.setBackgroundColor(Color.RED)

        BTNSignup!!.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}