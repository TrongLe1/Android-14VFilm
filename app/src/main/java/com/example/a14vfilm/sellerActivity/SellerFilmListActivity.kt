package com.example.a14vfilm.sellerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.a14vfilm.R

class SellerFilmListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_film_list)

        setSupportActionBarActivity()

    }

    private fun getIntentList(): Boolean{
        val check = intent.getStringExtra("currentCheck")
        return check.equals("true")
    }

    private fun setSupportActionBarActivity(){
        if (getIntentList())
            supportActionBar?.title = "Danh sách phim hiện tại"
        else
            supportActionBar?.title = "Danh sách phim đã hết hạn"
    }

}