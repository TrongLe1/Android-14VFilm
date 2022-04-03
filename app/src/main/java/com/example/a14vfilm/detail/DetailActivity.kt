package com.example.a14vfilm.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.*

class DetailActivity : AppCompatActivity() {
    var BTNFav: Button? = null
    var BTNOrder: Button? = null

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
        lifecycle.addObserver(YTPVTrailer!!)
        val film = Film(1, "1", "Scream 2022 - Tiếng Thét 2022", "Hans luôn tỏ ra là người làm chủ gia đình, nhưng thực chất không phải là một người mạnh mẽ. Kế hoạch về thăm nhà cũng khiến anh cảm thấy vô cùng áp lực bởi vợ và hai con luôn chậm chạp, còn người mẹ thì liên tục gọi điện hối thúc. Sự bức bối đó đã khiến Hans liều lĩnh tăng ga tối đa trên đường cao tốc và xảy ra mâu thuẫn với một người đàn ông nguy hiểm. Vô tình, hành trình về thăm ông bà bỗng hóa cuộc trốn chạy kẻ sát nhân điên rồ với cách thức giết người tàn ác.", 3.5F, 120, "Mỹ", Date(), 100000, 1, Date())
        TVName!!.text = film.name
        RBRate!!.rating = film.rate
        TVRateCount!!.text = "(40)"
        TVType!!.text = "Thể loại: "
        TVLength!!.text = "Thời lượng: " + film.length.toString() + " phút"
        TVCountry!!.text = "Nước sản xuất: " + film.country
        TVDatePublished!!.text = "Ngày công chiếu: " + film.datePublished.time.toString()
        TVPrice!!.text = "Giá thuê: " + film.price.toString() + " VNĐ/Ngày"
        TVQuantity!!.text = "| Số lượng: " + film.quantity.toString() + " cái"
        TVDescription!!.text = film.description
        YTPVTrailer!!.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo("7vpd7VjdQNQ", 0F);
            }
        })
        /*
        BTNFav = findViewById(R.id.BTNFavourite)
        BTNFav!!.setBackgroundColor(Color.TRANSPARENT)
        BTNFav!!.setTextColor(Color.RED)

        BTNOrder = findViewById(R.id.BTNOrder)
        BTNOrder!!.setBackgroundColor(Color.BLUE)
         */
        supportActionBar!!.hide()
    }
}