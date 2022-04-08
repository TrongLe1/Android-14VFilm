package com.example.a14vfilm.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.UserLogin
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    var BTNFav: Button? = null
    var BTNOrder: Button? = null

    var IVDetail: ImageView? = null
    var TVName: TextView? = null
    var TVRate: TextView? = null
    var RBRate: RatingBar? = null
    var TVType: TextView? = null
    var TVLength: TextView? = null
    var TVCountry: TextView? = null
    var TVDatePublished: TextView? = null
    var TVPrice: TextView? = null
    var TVQuantity: TextView? = null
    var TVDescription: TextView? = null
    var TVRateCount: TextView? = null
    var YTPVTrailer: YouTubePlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        IVDetail = findViewById(R.id.IVDetail)
        TVName = findViewById(R.id.TVDName)
        TVRate = findViewById(R.id.TVDRate)
        RBRate = findViewById(R.id.RBDRate)
        TVType = findViewById(R.id.TVDType)
        TVLength = findViewById(R.id.TVDLength)
        TVCountry = findViewById(R.id.TVDCountry)
        TVDatePublished = findViewById(R.id.TVDDPublished)
        TVPrice = findViewById(R.id.TVDPrice)
        TVQuantity = findViewById(R.id.TVDQuantity)
        TVDescription = findViewById(R.id.TVDDescription)
        TVRateCount = findViewById(R.id.TVRateCount)
        YTPVTrailer = findViewById(R.id.YTPVTrailer)
        BTNFav = findViewById(R.id.BTNFavourite)
        BTNOrder = findViewById(R.id.BTNBuy)
        lifecycle.addObserver(YTPVTrailer!!)
        val film = intent.getSerializableExtra("Film") as Film
        if (film.image != "")
            Picasso.get().load(film.image).resize(400, 360).into(IVDetail)
        TVName!!.text = film.name
        RBRate!!.rating = film.rate
        TVRateCount!!.text = "(40)"
        TVType!!.text = "Thể loại: "
        TVLength!!.text = "Thời lượng: " + film.length.toString() + " phút"
        TVCountry!!.text = "Nước sản xuất: " + film.country
        TVDatePublished!!.text = "Ngày công chiếu: " + SimpleDateFormat("dd/MM/yyy").format(film.datePublished)
        TVPrice!!.text = "Giá thuê: " + film.price.toString() + " VNĐ/Ngày"
        TVQuantity!!.text = "| Số lượng: " + film.quantity.toString() + " cái"
        TVDescription!!.text = film.description
        YTPVTrailer!!.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(film.trailer, 0F);
            }
        })

        BTNOrder!!.setOnClickListener {
            if(UserLogin.info != null) {
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("Film", film)
                startActivity(intent)
            }
        }

        supportActionBar!!.hide()
    }
}