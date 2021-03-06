package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.JoinerTableAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Transaction
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
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
    private var tvDetailDatePublished: TextView? = null
    private var tvDetailCommentCount: TextView? = null
    private var rbFilmSellerDetailRate: RatingBar? = null

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
    private var checkAdapter: Int? =  null
    private var transactionList: ArrayList<Transaction> = ArrayList()

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
            editFilm()
        }

        /*click trailer videoview to resume video*/
        vvFilmTrailer!!.setOnClickListener {
            vvFilmTrailer!!.resume()
        }

        /*click video videoview to resume video*/
        vvFilmVideo!!.setOnClickListener {
            vvFilmVideo!!.pause()
        }

    }

    /*function to change to SellerEditFilmActivity (edit detail film)*/
    private fun editFilm() {
        val intent = Intent(this,  SellerEditFilmActivity::class.java)
        intent.putExtra("Film", filmDetail)
        startActivityForResult(intent, 100)
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

        // set message of alert dialog
        dialogBuilder.setMessage("B???n c?? ch???p nh???n x??a phim n??y kh??ng?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Ch???p nh???n") { dialog, id ->

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
                            "X??a phim th??nh c??ng!",
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
            .setNegativeButton("H???y b???") { dialog, id ->
                Toast.makeText(this@SellerFilmDetailActivity,
                    "H???y b??? x??a phim...",
                    Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Th??ng b??o x??a phim")
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

        val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("transaction")
        dbReference.addListenerForSingleValueEvent( object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalRate: Float = "0".toFloat()
                var avg = 0
                var commentCount = 0
                for (singleSnapshot in snapshot.children) {
                    val film = singleSnapshot.child("film").getValue<String>()
                    if(film.toString().equals(filmDetail!!.id)){
                        val id = singleSnapshot.child("id").getValue<String>()
                        val user = singleSnapshot.child("user").getValue<String>()
                        val rentDate = singleSnapshot.child("rentDate").getValue<Date>()
                        val expired = singleSnapshot.child("expired").getValue<Date>()
                        val total = singleSnapshot.child("total").getValue<Long>()
                        val rate = singleSnapshot.child("rate").getValue<Float>()
                        val type = singleSnapshot.child("type").getValue<Boolean>()
                        val comment = singleSnapshot.child("comment").getValue<String>()
                        if(film == filmDetail!!.id) {
                            transactionList.add(Transaction(id!!,
                                user!!,
                                film!!,
                                rentDate!!,
                                expired!!,
                                total!!,
                                rate!!,
                                type!!,
                                comment!!))
                            totalRate += rate
                            avg+=1
                            if(comment != "")
                                commentCount++
                        }
                    }
                }
                
                rbFilmSellerDetailRate!!.rating = filmDetail!!.rate
                tvDetailCommentCount!!.text = "(C?? ${commentCount.toString()} l?????t ????nh gi??)"
                tvDetailName!!.text = filmDetail!!.name
                tvDetailDescription!!.text = filmDetail!!.description
                tvDetailPrice!!.text = "Gi?? thu??: ${filmDetail!!.price} VN??/Ng??y"
                tvDetailDuration!!.text = "Th???i l?????ng: ${filmDetail!!.length} ph??t"
                tvDetailCountry!!.text = "Qu???c gia: ${filmDetail!!.country}"

                if(SimpleDateFormat("dd/MM/yyyy").format(filmDetail!!.dateUpdated) != SimpleDateFormat("dd/MM/yyyy").format(
                        Date(0,0,0))
                    && SimpleDateFormat("dd/MM/yyyy").format(filmDetail!!.dateUpdated) != SimpleDateFormat("dd/MM/yyyy").format(
                        Date(0,0,1)))
                    tvDetailDatePublished!!.text = "Ng??y ????ng: ${SimpleDateFormat("dd/MM/yyyy").format(filmDetail!!.dateUpdated)}"


                var genre = "Th??? lo???i: "
                for (i in filmDetail!!.genre.indices) {
                    genre += "${filmDetail!!.genre[i]} "
                }

                tvDetailGenre!!.text = genre
                Picasso.get().load(filmDetail!!.image).into(tvDetailImage)

                vvFilmTrailer!!.setVideoPath(filmDetail!!.trailer)
                vvFilmTrailer!!.setOnPreparedListener {
                    vvFilmTrailer!!.pause()
                }


                vvFilmVideo!!.setVideoPath(filmDetail!!.video)
                vvFilmVideo!!.setOnPreparedListener {
                    vvFilmVideo!!.pause()
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        

    }

    /*init view component to variable*/
    private fun initViewComponent() {

        /*set view to variable*/
        tvDetailName = findViewById(R.id.tvFilmSellerDetailName)
        tvDetailDescription = findViewById(R.id.tvFilmSellerDetailDescription)
        tvDetailPrice = findViewById(R.id.tvFilmSellerDetailRentPrice)
        tvDetailDuration = findViewById(R.id.tvFilmSellerDetailDuration)
        tvDetailDatePublished = findViewById(R.id.tvFilmSellerDetailDatePublished)
        tvDetailCommentCount = findViewById(R.id.tvFilmSellerDetailRateCount)
        rbFilmSellerDetailRate = findViewById(R.id.rbFilmSellerDetailRate)

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
        checkAdapter = intent.getIntExtra("check", 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                filmDetail = data!!.getSerializableExtra("Film") as Film?
                this.recreate()
            }
        }
    }

}