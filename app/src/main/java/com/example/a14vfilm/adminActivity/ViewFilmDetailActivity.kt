package com.example.a14vfilm.adminActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.ViewFilmsAdapter
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.util.*

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
    private var ivFilmImage: ImageView? = null
    private var tvFilmRateCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_film_detail)

        initComponent()

        //get data from intent
        val filmID = intent.getStringExtra("filmID").toString()
        val filmSeller = intent.getStringExtra("filmSeller").toString()
        val filmName = intent.getStringExtra("filmName").toString()
        val filmRating = intent.getFloatExtra("filmRate", 4f)
        val filmPrice = intent.getIntExtra("filmPrice", 0).toString()
        val filmQuantity = intent.getIntExtra("filmQuantity", 0).toString()
        val filmType = intent.getStringExtra("filmGenre").toString()
        val filmLength = intent.getIntExtra("filmLength", 0).toString()
        val filmCountry = intent.getStringExtra("filmCountry").toString()
        val filmDPublished = intent.getSerializableExtra("filmDatePublished").toString()
        val filmDescription = intent.getStringExtra("filmDescription").toString()
        val filmImage =  intent.getStringExtra("filmImage").toString()
        val filmRateCount =  intent.getIntExtra("filmRateCount", 0).toString()

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
        tvFilmRateCount!!.append(filmRateCount)

        //set image for film
        if (filmImage != "")
            Picasso.get().load(filmImage).resize(150, 150).into(ivFilmImage!!)

//        findViewById<Button>(R.id.viewfilmdetail_deleteFilm).setOnClickListener{
//            //find seller of film to mail and delete film
//            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
//            val ref = FirebaseDatabase.getInstance(url).getReference()
//            val query = ref.child("film").child(filmID)
//            //find seller
//            val userID:String = query.child("seller").key.toString()
//            val userEmail:String = ref.child("user").child(userID).child("email").key.toString()
//            //send email
//            val intent1 = Intent(Intent.ACTION_SENDTO)
//            intent1.putExtra(Intent.EXTRA_SUBJECT, "Tài khoản 14VFilm của bạn đã bị xóa");
//            intent1.putExtra(Intent.EXTRA_TEXT, "Tài khoản của bạn đã bị xóa vì vi phạm chính sách người dùng");
//            intent1.setData(Uri.parse("mailto:" + userEmail));
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//            startActivity(intent1)
//
//            //delete film
//            query.removeValue()
//            Toast.makeText(it.context, "Xóa film thành công", Toast.LENGTH_SHORT).show()
//
//
//
//
//        }
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
          ivFilmImage = findViewById(R.id.viewfilmdetail_IVDetail)
          tvFilmRateCount = findViewById(R.id.viewfilmdetail_TVRateCount)
    }
}