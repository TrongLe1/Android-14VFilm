package com.example.a14vfilm.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FavoriteAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.util.*
import kotlin.collections.ArrayList

class FavoriteActivity : AppCompatActivity() {
    var RVFavorite: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar!!.hide()

        RVFavorite = findViewById(R.id.RVFavorite)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RVFavorite!!.layoutManager = layoutManager
        RVFavorite!!.addItemDecoration(LayoutMarginDecoration(1, 20))
        val favList = ArrayList<FavoriteExtend>()
        var favoriteAdapter = FavoriteAdapter(favList)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("favorite")
        val query = ref.orderByChild("user").equalTo(UserLogin.info!!.id)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val favId = singleSnapshot.child("id").getValue<String>()
                    val user = singleSnapshot.child("user").getValue<String>()
                    val film = singleSnapshot.child("film").getValue<String>()
                    val sRef = FirebaseDatabase.getInstance(url).getReference("film")
                    val sQuery = sRef.orderByChild("id").equalTo(film)
                    sQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (singleSnapshot in snapshot.children) {
                                val id = singleSnapshot.child("id").getValue<String>()
                                val seller = singleSnapshot.child("seller").getValue<String>()
                                val name = singleSnapshot.child("name").getValue<String>()
                                val description =
                                    singleSnapshot.child("description").getValue<String>()
                                val rate = singleSnapshot.child("rate").getValue<Float>()
                                val length = singleSnapshot.child("length").getValue<Int>()
                                val country = singleSnapshot.child("country").getValue<String>()
                                val datePublished =
                                    singleSnapshot.child("datePublished").getValue<Date>()
                                val price = singleSnapshot.child("price").getValue<Int>()
                                //val quantity = singleSnapshot.child("quantity").getValue<Int>()
                                val dateUpdated =
                                    singleSnapshot.child("dateUpdated").getValue<Date>()
                                val image = singleSnapshot.child("image").getValue<String>()
                                val trailer = singleSnapshot.child("trailer").getValue<String>()
                                val genreList =
                                    singleSnapshot.child("genre").getValue<ArrayList<String>>()
                                val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                                val status = singleSnapshot.child("status").getValue<Boolean>()
                                if (status == true)
                                    favList.add(
                                        FavoriteExtend(
                                            Favorite(favId!!, user!!, film!!),
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
                                                ""
                                            )
                                        )
                                    )
                            }
                            RVFavorite!!.adapter = favoriteAdapter
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })

                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        favoriteAdapter.onItemClick = {film ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("Film", film)
            startActivityForResult(intent, 100)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.recreate()
    }
}