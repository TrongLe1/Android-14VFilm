package com.example.a14vfilm.sellerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.*
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film

class SellerEditFilmActivity : AppCompatActivity() {

    /*init variable from view of activity*/
    private var etEditName: EditText? = null
    private var etEditDescription: EditText? = null
    private var tvEditGenre: TextView? = null
    private var tvEditCountry: TextView? = null
    private var etEditPrice: EditText? = null
    private var etEditDuration: EditText? = null

    private var ivEditImage: ImageView? = null
    private var vvFilmTrailer: VideoView? = null
    private var vvFilmVideo: VideoView? = null

    private var btnSelectTrailer: Button? = null
    private var btnSelectVideo: Button? = null
    private var btnSaveChange: Button? = null

    /*init variable stored detail film*/
    private var filmDetail: Film? = null

    /*media controller to play video*/
    private var trailerMediaController: MediaController? = null
    private var videoMediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_edit_film)

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

        /*handling click to save change of detail film*/
        btnSaveChange!!.setOnClickListener {
            saveFilmDetailChange()
        }

        /*handling click to choose new trailer*/
        btnSelectTrailer!!.setOnClickListener {

        }

        /*handling click to choose new video*/
        btnSelectVideo!!.setOnClickListener {

        }

    }

    /*save to Firebase Database and Storage*/
    private fun saveFilmDetailChange() {
        TODO("Not yet implemented")
    }

    /*display film detail stored in film variable to view*/
    private fun displayDataToViewComponent() {

        etEditName!!.text = SpannableStringBuilder(filmDetail!!.name)


    }

    /*init view component to variable*/
    private fun initViewComponent() {

        /*set view to variable*/
        etEditName = findViewById(R.id.etFilmEditDetailName)
        etEditDescription = findViewById(R.id.etFilmEditDetailDescription)
        etEditPrice = findViewById(R.id.etFilmEditDetailRentPrice)
        etEditDuration = findViewById(R.id.etFilmEditDetailDuration)

        tvEditCountry = findViewById(R.id.tvFilmEditDetailCountry)
        tvEditGenre = findViewById(R.id.tvFilmEditDetailGenre)

        ivEditImage = findViewById(R.id.ivFilmEditDetailImage)
        vvFilmTrailer =findViewById(R.id.vvVideoUpload)
        vvFilmVideo = findViewById(R.id.vvFullVideoUpload)

        btnSelectTrailer = findViewById(R.id.btnSelectTrailer)
        btnSelectVideo = findViewById(R.id.btnSelectFullVideo)
        btnSaveChange = findViewById(R.id.btnSaveChange)

        /*set up Media Controller*/
        trailerMediaController = MediaController(this)
        trailerMediaController!!.setAnchorView(vvFilmTrailer)
        vvFilmTrailer!!.setMediaController(trailerMediaController)

        videoMediaController = MediaController(this)
        videoMediaController!!.setAnchorView(vvFilmVideo)
        vvFilmVideo!!.setMediaController(videoMediaController)

    }

    /*get data from previous activity*/
    private fun getDataIntent() {
        filmDetail = intent.getSerializableExtra("Film") as Film?
    }


}