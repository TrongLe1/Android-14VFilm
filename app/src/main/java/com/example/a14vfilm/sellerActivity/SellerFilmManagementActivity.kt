package com.example.a14vfilm.sellerActivity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmSellerManagementAdapter
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SellerFilmManagementActivity : AppCompatActivity() {

    private var rcvListFilmManagement: RecyclerView? = null

        @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_film_management)

        initComponent()

        val filmList = ArrayList<Film>()
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        val adapterListFilmManagement = FilmSellerManagementAdapter(filmList)

        val query = ref.orderByChild("dateUpdated")
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val id = singleSnapshot.child("id").getValue<String>()
                    val seller = singleSnapshot.child("seller").getValue<String>()
                    val name = singleSnapshot.child("name").getValue<String>()
                    val description = singleSnapshot.child("description").getValue<String>()
                    val rate = singleSnapshot.child("rate").getValue<Float>()
                    val length = singleSnapshot.child("length").getValue<Int>()
                    val country = singleSnapshot.child("country").getValue<String>()
                    val datePublished = singleSnapshot.child("datePublished").getValue<Date>()
                    val price = singleSnapshot.child("price").getValue<Int>()
                    val quantity = singleSnapshot.child("quantity").getValue<Int>()
                    val dateUpdated = singleSnapshot.child("dateUpdated").getValue<Date>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val trailer = singleSnapshot.child("trailer").getValue<String>()
                    val genreList = singleSnapshot.child("genre").getValue<ArrayList<String>>()
                    val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, quantity!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!))
                }

                rcvListFilmManagement!!.adapter = adapterListFilmManagement
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    private fun initComponent(){
        rcvListFilmManagement = findViewById(R.id.rcvFilmManagement)
        rcvListFilmManagement!!.layoutManager = LinearLayoutManager(this)
    }
}