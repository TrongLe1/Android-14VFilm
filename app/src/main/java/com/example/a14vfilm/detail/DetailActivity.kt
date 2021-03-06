package com.example.a14vfilm.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {
    var BTNComment: Button? = null
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
    var YTPVTrailer: VideoView? = null
    var quantity: Int? = null
    var IBFavorite: ImageButton? = null
    var trailerMediaController: MediaController? = null

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
        BTNComment = findViewById(R.id.BTNComment)
        BTNOrder = findViewById(R.id.BTNBuy)
        IBFavorite = findViewById(R.id.IBFavorite)
        //lifecycle.addObserver(YTPVTrailer!!)
        val film = intent.getSerializableExtra("Film") as Film

        //check if intent from admin
        val checkAdmin = intent.getBooleanExtra("admin",false)
        if (checkAdmin){
            BTNOrder!!.visibility = View.GONE
            IBFavorite!!.visibility = View.GONE
            BTNComment!!.visibility = View.GONE
        }

        //quantity = film.quantity
        if (film.image != "")
            Picasso.get().load(film.image).resize(400, 360).into(IVDetail)
        TVName!!.text = film.name
        RBRate!!.rating = film.rate
        TVRateCount!!.text = "("+ film.rateTime + " l?????t ????nh gi??)"
        TVType!!.text = "Th??? lo???i: "
        for (item in film.genre)
            TVType!!.text = TVType!!.text.toString() + item + " "
        TVLength!!.text = "Th???i l?????ng: " + film.length.toString() + " ph??t"
        TVCountry!!.text = "N?????c s???n xu???t: " + film.country
        TVDatePublished!!.text = "Ng??y c??ng chi???u: " + SimpleDateFormat("dd/MM/yyy").format(film.datePublished)
        val formatter = DecimalFormat("#,###")
        TVPrice!!.text = "Gi?? thu??: " + formatter.format(film.price) + " VN??/Ng??y"
        //TVQuantity!!.text = "| S??? l?????ng: " + film.quantity.toString() + " c??i"

        /*
        if (film.quantity > 0)
            TVQuantity!!.text = "| T??nh tr???ng: C??n h??ng"
        else {
            TVQuantity!!.text = "| T??nh tr???ng: H???t h??ng"
            BTNOrder!!.isClickable = false
            BTNOrder!!.isEnabled = false
        }

        */

        TVDescription!!.text = film.description

        trailerMediaController = MediaController(this)
        trailerMediaController!!.setAnchorView(YTPVTrailer)
        YTPVTrailer!!.setMediaController(trailerMediaController)
        YTPVTrailer!!.setVideoPath(film.trailer)
        YTPVTrailer!!.requestFocus()
        YTPVTrailer!!.setOnPreparedListener {
            YTPVTrailer!!.pause()
        }

        /*
        YTPVTrailer!!.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(film.trailer, 0F);
            }
        })

        */
        if (UserLogin.info != null) {
            val query = ref.orderByChild("user").equalTo(UserLogin.info!!.id)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        val id = singleSnapshot.child("film").getValue<String>()
                        if (film.id == id) {
                            checkFav = true
                            IBFavorite!!.setImageDrawable(resources.getDrawable(R.drawable.red_heart))
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
        IBFavorite!!.setOnClickListener {
            if(UserLogin.info != null) {
                if (!checkFav) {
                    val fav = Favorite(ref.push().key!!, UserLogin.info!!.id, film.id)
                    ref.child(fav.id).setValue(fav)
                    Toast.makeText(this, "???? th??m v??o y??u th??ch", Toast.LENGTH_SHORT).show()
                    //BTNFav!!.setCompoundDrawablesWithIntrinsicBounds(BTNFav!!.context.resources.getDrawable(R.drawable.red_heart), null, null, null)
                    IBFavorite!!.setImageDrawable(resources.getDrawable(R.drawable.red_heart))
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
                    Toast.makeText(this, "???? x??a kh???i y??u th??ch", Toast.LENGTH_SHORT).show()
                    //BTNFav!!.setCompoundDrawablesWithIntrinsicBounds(BTNFav!!.context.resources.getDrawable(R.drawable.white_heart), null, null, null)
                    IBFavorite!!.setImageDrawable(resources.getDrawable(R.drawable.white_heart))
                    checkFav = false
                }
            }
            else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        BTNComment!!.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("ID", film.id)
            startActivity(intent)
        }
        supportActionBar!!.hide()
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                quantity = quantity!! - 1
                TVQuantity!!.text = "| S??? l?????ng: " + quantity.toString() + " c??i"
            }
        }
    }
    */
}