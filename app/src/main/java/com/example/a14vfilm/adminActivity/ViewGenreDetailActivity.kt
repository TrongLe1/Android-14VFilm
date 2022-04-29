package com.example.a14vfilm.adminActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.a14vfilm.R
import com.squareup.picasso.Picasso

class ViewGenreDetailActivity : AppCompatActivity() {
    private var tvGenreName: EditText? = null
    private var ivGenreImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_genre_detail)

        initComponent()

        //get data from intent
        val genreID = intent.getStringExtra("genreID").toString()
        val genreName = intent.getStringExtra("genreName").toString()
        val genreImage =  intent.getStringExtra("genreImage").toString()

        //set layout with user information
        tvGenreName!!.setText(genreName)

        //set image for genre
        if (genreImage != "")
            Picasso.get().load(genreImage).resize(150, 150).into(ivGenreImage!!)

//        findViewById<Button>(R.id.viewgenredetail_deleteFilm).setOnClickListener{
//            //find seller of film to mail and delete film
//            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
//            val ref = FirebaseDatabase.getInstance(url).getReference()
//            val query = ref.child("Genre").child(filmID)
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
        tvGenreName = findViewById(R.id.viewgenredetail_TVDName)
        ivGenreImage = findViewById(R.id.viewgenredetail_IVDetail)

    }
}