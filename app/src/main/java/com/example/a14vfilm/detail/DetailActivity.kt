package com.example.a14vfilm.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.a14vfilm.R
import com.example.a14vfilm.login.LoginActivity
import com.example.a14vfilm.models.Favorite
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
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
    var quantity: Int? = null

    private val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val ref = FirebaseDatabase.getInstance(url).getReference("favorite")
    private var checkFav = false

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
        quantity = film.quantity
        if (film.image != "")
            Picasso.get().load(film.image).resize(400, 360).into(IVDetail)
        TVName!!.text = film.name
        RBRate!!.rating = film.rate
        TVRateCount!!.text = "("+ film.rateTime + " lượt đánh giá)"
        TVType!!.text = "Thể loại: "
        for (item in film.genre)
            TVType!!.text = TVType!!.text.toString() + item + " "
        TVLength!!.text = "Thời lượng: " + film.length.toString() + " phút"
        TVCountry!!.text = "Nước sản xuất: " + film.country
        TVDatePublished!!.text = "Ngày công chiếu: " + SimpleDateFormat("dd/MM/yyy").format(film.datePublished)
        TVPrice!!.text = "Giá thuê: " + film.price.toString() + " VNĐ/Ngày"
        //TVQuantity!!.text = "| Số lượng: " + film.quantity.toString() + " cái"

        if (film.quantity > 0)
            TVQuantity!!.text = "| Tình trạng: Còn hàng"
        else {
            TVQuantity!!.text = "| Tình trạng: Hết hàng"
            BTNOrder!!.isClickable = false
            BTNOrder!!.isEnabled = false
        }

        TVDescription!!.text = film.description
        YTPVTrailer!!.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(film.trailer, 0F);
            }
        })
        if (UserLogin.info != null) {
            val query = ref.orderByChild("user").equalTo(UserLogin.info!!.id)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        val id = singleSnapshot.child("film").getValue<String>()
                        if (film.id == id) {
                            checkFav = true
                            BTNFav!!.setCompoundDrawablesWithIntrinsicBounds(
                                BTNFav!!.context.resources.getDrawable(
                                    R.drawable.red_heart
                                ), null, null, null
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        BTNOrder!!.setOnClickListener {
            if(UserLogin.info != null) {
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("Film", film)
                startActivityForResult(intent, 1000)
            }
            else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        BTNFav!!.setOnClickListener {
            if(UserLogin.info != null) {
                if (!checkFav) {
                    val fav = Favorite(ref.push().key!!, UserLogin.info!!.id, film.id)
                    ref.child(fav.id).setValue(fav)
                    Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    BTNFav!!.setCompoundDrawablesWithIntrinsicBounds(BTNFav!!.context.resources.getDrawable(R.drawable.red_heart), null, null, null)
                    checkFav = true
                }
                else {
                    val query = ref.orderByChild("user").equalTo(UserLogin.info!!.id)
                    query.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (singleSnapshot in snapshot.children) {
                                val id = singleSnapshot.child("film").getValue<String>()
                                if (id == film.id) singleSnapshot.ref.removeValue()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                    Toast.makeText(this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                    BTNFav!!.setCompoundDrawablesWithIntrinsicBounds(BTNFav!!.context.resources.getDrawable(R.drawable.white_heart), null, null, null)
                    checkFav = false
                }
            }
            else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        supportActionBar!!.hide()
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                quantity = quantity!! - 1
                TVQuantity!!.text = "| Số lượng: " + quantity.toString() + " cái"
            }
        }
    }
    */
}