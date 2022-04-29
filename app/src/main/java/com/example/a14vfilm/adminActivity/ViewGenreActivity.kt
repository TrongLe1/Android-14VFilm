package com.example.a14vfilm.adminActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.ViewGenreAdapter
import com.example.a14vfilm.models.Genre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

class ViewGenreActivity : AppCompatActivity() {
    private var rcvViewGenre: RecyclerView? = null
    private var genreSearch: androidx.appcompat.widget.SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_genre)

        // Initialize the required components
        initComponent()

        //create a list of user for adapter
        val genreList = ArrayList<Genre>()
        //url for firebase database (change later)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("genre")
        val adapterViewGenre = ViewGenreAdapter(genreList)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                genreList.clear()
                for (singleSnapshot in snapshot.children) {
                    val id = singleSnapshot.child("id").getValue<String>()
                    val name = singleSnapshot.child("name").getValue<String>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    genreList.add(0, Genre(id!!, name!!, image!!))
                }

                rcvViewGenre!!.adapter = adapterViewGenre
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        rcvViewGenre!!.adapter = adapterViewGenre

        genreSearch!!.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterViewGenre.filter.filter(newText)
                return false
            }

        })
    }

    private fun initComponent(){
        rcvViewGenre = findViewById(R.id.viewgenre_rcvGenreManagement)
        genreSearch = findViewById(R.id.viewgenre_svGenreSearch)
        rcvViewGenre!!.layoutManager = LinearLayoutManager(this)
    }
}