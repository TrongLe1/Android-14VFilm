package com.example.a14vfilm.adminActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.a14vfilm.R
import com.google.firebase.database.FirebaseDatabase

class ViewFilmDetailActivity : AppCompatActivity() {

    private var tvFilmName: TextView? = null
    private var tvFilmRating: RatingBar? = null
    private var tvFilmPrice: TextView? = null
    private var tvFilmQuantity: TextView? = null
    private var tvFilmType: TextView? = null
    private var tvFilmLength: TextView? = null
    private var tvFilmCountry: TextView? = null
    private var tvFilmDPublished: TextView? = null
    private var tvFilmDescription: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_film_detail)

        initComponent()

        //get data from intent
        val filmID = intent.getStringExtra("filmID").toString()
        val filmName = intent.getStringExtra("filmName").toString()
        val filmRating = intent.getFloatExtra("filmRate", 4f)
        val filmPrice = intent.getIntExtra("filmPrice", 0).toString()
        val filmQuantity = intent.getIntExtra("filmQuantity", 0).toString()
        val filmType = intent.getStringExtra("filmGenre").toString()
        val filmLength = intent.getIntExtra("filmLength", 0).toString()
        val filmCountry = intent.getStringExtra("filmCountry").toString()
        val filmDPublished = intent.getSerializableExtra("filmDatePublished").toString()
        val filmDescription = intent.getStringExtra("filmDescription").toString()

        //set layout with user information
        tvFilmName!!.text = filmName
        tvFilmRating!!.rating = filmRating
        tvFilmPrice!!.append(filmPrice + " đồng")
        tvFilmQuantity!!.append(filmQuantity)
        tvFilmType!!.append(filmType)
        tvFilmLength!!.append(filmLength + " phút")
        tvFilmCountry!!.append(filmCountry)
        tvFilmDPublished!!.append(filmDPublished)
        tvFilmDescription!!.text = filmDescription

        findViewById<Button>(R.id.viewfilmdetail_deleteFilm).setOnClickListener{
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference()
            val query = ref.child("film").child(filmID)
            query.removeValue()
            Toast.makeText(it.context, "Xóa film thành công", Toast.LENGTH_SHORT).show()
            val intent = Intent(it.context, ViewFilmsActivityAdmin::class.java)
            startActivity(intent)
        }
    }

    private fun initComponent(){
          tvFilmName = findViewById(R.id.viewfilmdetail_TVDName)
          tvFilmRating = findViewById(R.id.viewfilmdetail_RBDRate)
          tvFilmPrice = findViewById(R.id.viewfilmdetail_TVDPrice)
          tvFilmQuantity = findViewById(R.id.viewfilmdetail_TVDQuantity)
          tvFilmType = findViewById(R.id.viewfilmdetail_TVDType)
          tvFilmLength = findViewById(R.id.viewfilmdetail_TVDLength)
          tvFilmCountry = findViewById(R.id.viewfilmdetail_TVDCountry)
          tvFilmDPublished = findViewById(R.id.viewfilmdetail_TVDDPublished)
          tvFilmDescription = findViewById(R.id.viewfilmdetail_TVDDescription)
    }
}