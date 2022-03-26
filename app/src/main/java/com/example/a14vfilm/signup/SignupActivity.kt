package com.example.a14vfilm.signup

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.a14vfilm.R

class SignupActivity : AppCompatActivity() {
    var BTNSignup: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar!!.hide()
        BTNSignup = findViewById(R.id.BTNSignupDone)
        BTNSignup!!.setTextColor(Color.WHITE)
        BTNSignup!!.setBackgroundColor(Color.RED)
    }
}