package com.example.a14vfilm.adminActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.ViewFilmsAdapter
import com.example.a14vfilm.adapters.ViewUserAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.*

class ViewFilmsActivityAdmin : AppCompatActivity() {
    private var rcvViewFilm: RecyclerView? = null
    private var filmSearch: androidx.appcompat.widget.SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_films_admin)

        // Initialize the required components
        initComponent()
        supportActionBar!!.hide()

        //create a list of user for adapter
        val filmList = ArrayList<Film>()
        //url for firebase database (change later)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        var adapterViewFilm = ViewFilmsAdapter(filmList)

        rcvViewFilm!!.adapter = adapterViewFilm
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

                    if (SimpleDateFormat("dd/MM/yyy").format(dateUpdated!!) != SimpleDateFormat("dd/MM/yyy").format(Date(0,0,0))) {
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
                rcvViewFilm!!.adapter!!.notifyDataSetChanged()


            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //Filter
                val option = arrayListOf("A-Z", "Đang hoạt động", "Đã bị ẩn")
                val SSort = findViewById<Spinner>(R.id.viewfilm_SSort)
                val sAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, option)
                sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                SSort.adapter = sAdapter
                SSort.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        filmList.clear()
                        getFilterLibrary(SSort.selectedItem.toString(), rcvViewFilm!!, filmList, adapterViewFilm)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }


        filmSearch!!.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterViewFilm.filter.filter(newText)
                return false
            }

        })
    }

    private fun initComponent(){
        rcvViewFilm = findViewById(R.id.viewfilm_rcvFilmsManagement)
        filmSearch = findViewById(R.id.viewfilm_svFilmSearch)
        filmSearch!!.setFocusable(false);
        rcvViewFilm!!.layoutManager = LinearLayoutManager(this)

    }
    private fun getFilterLibrary(sort: String, RVLibrary: RecyclerView, filmList: ArrayList<Film>, libraryAdapter: ViewFilmsAdapter) {
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        if (sort == "A-Z") {
            RVLibrary.adapter = libraryAdapter
            ref.orderByChild("name").addValueEventListener(object: ValueEventListener {
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
                        if (SimpleDateFormat("dd/MM/yyy").format(dateUpdated!!) != SimpleDateFormat("dd/MM/yyy").format(Date(0,0,0))) {
                            filmList.add(
                                0,
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
                    RVLibrary.adapter!!.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
        else if (sort == "Đang hoạt động") {
            RVLibrary.adapter = libraryAdapter
            ref.addValueEventListener(object: ValueEventListener {
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
                        if (SimpleDateFormat("dd/MM/yyy").format(dateUpdated!!) != SimpleDateFormat("dd/MM/yyy").format(Date(0,0,0)) && status == true) {
                            filmList.add(
                                0,
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
                    RVLibrary.adapter!!.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
        else {
            RVLibrary.adapter = libraryAdapter
            ref.addValueEventListener(object: ValueEventListener {
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
                        if (SimpleDateFormat("dd/MM/yyy").format(dateUpdated!!) != SimpleDateFormat("dd/MM/yyy").format(Date(0,0,0)) && status == false) {
                            filmList.add(
                                0,
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
                    RVLibrary.adapter!!.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

    }
}