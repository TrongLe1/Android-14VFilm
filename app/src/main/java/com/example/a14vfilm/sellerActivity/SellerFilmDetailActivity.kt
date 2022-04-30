package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*


class SellerFilmDetailActivity : AppCompatActivity() {

    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    /*init variable from view of activity*/
    private var tvDetailName: TextView? = null
    private var tvDetailDescription: TextView? = null
    private var tvDetailGenre: TextView? = null
    private var tvDetailCountry: TextView? = null
    private var tvDetailPrice: TextView? = null
    private var tvDetailDuration: TextView? = null

    private var tvDetailImage: ImageView? = null
    private var vvFilmTrailer: VideoView? = null
    private var vvFilmVideo: VideoView? = null

    private var btnEditFilm: Button? = null
    private var btnDeleteFilm: Button? = null

    private var rcvDetailJoiner: RecyclerView? = null

    /*init variable stored detail film*/
    private var filmDetail: Film? = null

    /*media controller to play video*/
    private var trailerMediaController: MediaController? = null
    private var videoMediaController: MediaController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_film_detail)


        /*Init*/
        initViewComponent()

        /*get data from previous activity*/
        getDataIntent()
        displayDataToViewComponent()

        /*handling button*/
        handleButtonClick()

    }

    /*handling action of button component*/
    private fun handleButtonClick() {

        /*handling click to delete this film*/
        btnDeleteFilm!!.setOnClickListener {
            deleteFilm()
        }

        /*handling click to edit this film*/
        btnEditFilm!!.setOnClickListener {

        }

//        vvFilmTrailer!!.setOnClickListener {
//            vvFilmTrailer!!.pause()
//        }
    }

    /*handling click delete film*/
    private fun deleteFilm() {

        /*notify to renter through renters' emails*/
//        notifyToAllRenter()

        /*delete film from Firebase*/
        deleteFilmFromFirebaseDatabase(filmDetail)

    }

    /*delete from Firebase*/
    private fun deleteFilmFromFirebaseDatabase(filmDetail: Film?) {

        val dialogBuilder = AlertDialog.Builder(this)

//        Log.e("ppppp", "${filmDetail!!.id}")

        // set message of alert dialog
        dialogBuilder.setMessage("Bạn có chấp nhận xóa phim này không?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Chấp nhận") { dialog, id ->

                /*config to path of film*/
                val ref = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("film")
                    .child(filmDetail!!.id)

                /*add listerner to delete data from Firebase*/
                ref.addListenerForSingleValueEvent(object : ValueEventListener {

                    /*delete successfully*/
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        /*delete data of film*/
                        ref.removeValue()

                        /*Notification*/
                        Toast.makeText(this@SellerFilmDetailActivity,
                            "Xóa phim thành công!",
                            Toast.LENGTH_SHORT).show()
                    }

                    /*delete failed*/
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Database Error:","onCancelled", databaseError.toException())
                    }
                })

                finish()
            }
            // negative button text and action
            .setNegativeButton("Hủy bỏ") { dialog, id ->
                Toast.makeText(this@SellerFilmDetailActivity,
                    "Hủy bỏ xóa phim...",
                    Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Thông báo xóa phim")
        // show alert dialog
        alert.show()

    }

    /*send email to Renter*/
    private fun notifyToAllRenter() {
        TODO("Not yet implemented")
    }

    /*display film detail stored in film variable to view*/
    @SuppressLint("SetTextI18n")
    private fun displayDataToViewComponent() {

        tvDetailName!!.text = filmDetail!!.name
        tvDetailDescription!!.text = filmDetail!!.description
        tvDetailPrice!!.text = "Giá thuê: ${filmDetail!!.price} VNĐ/Ngày"
        tvDetailDuration!!.text = "Thời lượng: ${filmDetail!!.length} phút"
        tvDetailCountry!!.text = "Quốc gia: ${filmDetail!!.country}"

        var genre = "Thể loại: "
        for (i in filmDetail!!.genre.indices) {
            genre += "${filmDetail!!.genre[i]} "
        }

        tvDetailGenre!!.text = genre
        Picasso.get().load(filmDetail!!.image).into(tvDetailImage)

        vvFilmTrailer!!.setVideoPath(filmDetail!!.trailer)
        vvFilmTrailer!!.setOnPreparedListener {
            vvFilmTrailer!!.pause()
        }


//        vvFilmVideo!!.setVideoPath(filmDetail!!.video)
//        vvFilmVideo!!.setOnPreparedListener {
//            vvFilmVideo!!.pause()
//        }

    }

    /*init view component to variable*/
    private fun initViewComponent() {

        /*set view to variable*/
        tvDetailName = findViewById(R.id.tvFilmSellerDetailName)
        tvDetailDescription = findViewById(R.id.tvFilmSellerDetailDescription)
        tvDetailPrice = findViewById(R.id.tvFilmSellerDetailRentPrice)
        tvDetailDuration = findViewById(R.id.tvFilmSellerDetailDuration)

        tvDetailCountry = findViewById(R.id.tvFilmSellerDetailCountry)
        tvDetailGenre = findViewById(R.id.tvFilmSellerDetailGenre)

        tvDetailImage = findViewById(R.id.ivSellerFilmDetailImage)
        tvDetailImage!!.requestFocus()
        vvFilmTrailer = findViewById(R.id.vvSellerDetailTrailer)
        vvFilmVideo = findViewById(R.id.vvSellerDetailFullVideo)

        btnEditFilm = findViewById(R.id.btnEditFilm)
        btnDeleteFilm = findViewById(R.id.btnDeleteFilm)

        /*set up Media Controller*/
        trailerMediaController = MediaController(this)
        trailerMediaController!!.setAnchorView(vvFilmTrailer!!)
        vvFilmTrailer!!.setMediaController(trailerMediaController)

        videoMediaController = MediaController(this)
        videoMediaController!!.setAnchorView(vvFilmVideo!!)
        vvFilmVideo!!.setMediaController(videoMediaController)

    }

    /*get data from previous activity*/
    private fun getDataIntent() {
        filmDetail = intent.getSerializableExtra("Film") as Film?
    }

}