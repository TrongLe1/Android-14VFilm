package com.example.a14vfilm.adminActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.VerifyFilmAdapter
import com.example.a14vfilm.adapters.ViewFilmsAdapter
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

class VerifyFilmAdminActivity : AppCompatActivity() {
    private var rcvVerifyFilm: RecyclerView? = null
    private var filmSearch: androidx.appcompat.widget.SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_film_admin)

        // Initialize the required components
        initComponent()
        supportActionBar!!.hide()

        //create a list of user for adapter
        val filmList = ArrayList<Film>()
        //url for firebase database (change later)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        var adapterVerifyFilm = VerifyFilmAdapter(filmList)

        rcvVerifyFilm!!.adapter = adapterVerifyFilm
        ref.orderByChild("name").addValueEventListener(object : ValueEventListener {
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
                    //val quantity = singleSnapshot.child("quantity").getValue<Int>()
                    val dateUpdated = singleSnapshot.child("dateUpdated").getValue<Date>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val trailer = singleSnapshot.child("trailer").getValue<String>()
                    val genreList = singleSnapshot.child("genre").getValue<ArrayList<String>>()
                    val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                    val status = singleSnapshot.child("status").getValue<Boolean>()
                    val video = singleSnapshot.child("video").getValue<String>()

                    if (dateUpdated == null) {
                        filmList.add(
                            Film(
                                id!!,
                                seller!!,
                                name!!,
                                description!!,
                                rate!!,
                                length!!,
                                country!!,
                                datePublished!!,
                                price!!,
                                dateUpdated!!,
                                image!!,
                                trailer!!,
                                genreList!!,
                                rateTime!!,
                                status!!,
                                video!!
                            )
                        )
                    }
                }
                rcvVerifyFilm!!.adapter!!.notifyDataSetChanged()


            }
            override fun onCancelled(error: DatabaseError) {}
        })


        filmSearch!!.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterVerifyFilm.filter.filter(newText)
                return false
            }

        })
    }

    private fun initComponent(){
        rcvVerifyFilm = findViewById(R.id.verifyfilm_rcvFilmsManagement)
        filmSearch = findViewById(R.id.verifyfilm_svFilmSearch)
        filmSearch!!.setFocusable(false);
        rcvVerifyFilm!!.layoutManager = LinearLayoutManager(this)

    }
}