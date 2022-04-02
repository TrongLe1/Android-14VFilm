package com.example.a14vfilm.sellerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.a14vfilm.R

class SellerUploadFilmActivity : AppCompatActivity() {
    var filmImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_upload_film)

        filmImage = findViewById(R.id.ivFilmImage)
        filmImage!!.setOnClickListener{
            Log.e("Ppppp", "True")
        }

    }
}