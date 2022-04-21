package com.example.a14vfilm.adminActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.ViewFilmsAdapter
import com.example.a14vfilm.adapters.ViewUserAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

class ViewFilmsActivityAdmin : AppCompatActivity() {
    private var rcvViewFilm: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_films_admin)

        // Initialize the required components
        initComponent()

        //create a list of user for adapter
        val filmList = ArrayList<Film>()
        //url for firebase database (change later)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        val adapterViewFilm = ViewFilmsAdapter(filmList)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                filmList.clear()
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
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, quantity!!, dateUpdated!!, image!!, trailer!!, genreList!!,rateTime!!))
                }

                rcvViewFilm!!.adapter = adapterViewFilm
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        rcvViewFilm!!.adapter = adapterViewFilm

    }

    private fun initComponent(){
        rcvViewFilm = findViewById(R.id.viewfilm_rcvFilmsManagement)
        rcvViewFilm!!.layoutManager = LinearLayoutManager(this)

    }
}