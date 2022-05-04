package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.CurrentSellerAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SellerFilmListActivity : AppCompatActivity() {

    /*constant url of Firebase*/
    private val FIREBASE_DATABASE_URL = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"

    /*init variable for view*/
    private var rcvListManagement: RecyclerView? = null

    private val ref = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("film")

    /*init array save film*/
    private val newFilm = ArrayList<Film>()
    private var adapter: CurrentSellerAdapter? = null
    private var checkAdapter: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_film_list)

        /*Init view component*/
        InitViewComponent()
        setSupportActionBarActivity()
        when(checkAdapter){
//            1,2,3 -> adapter = CurrentSellerAdapter(newFilm)
            1 -> adapter = CurrentSellerAdapter(newFilm, 1)
            2 -> adapter = CurrentSellerAdapter(newFilm, 2)
            3 -> adapter = CurrentSellerAdapter(newFilm, 3)
        }

        /*accessing to get film data of Firebase Database*/

        rcvListManagement!!.layoutManager = LinearLayoutManager(this)
        rcvListManagement!!.addItemDecoration(LayoutMarginDecoration(1, 20))
//        val query = ref.orderByChild("dateUpdated").limitToLast(5)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            @SuppressLint("SimpleDateFormat")
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
//                    val quantity = singleSnapshot.child("quantity").getValue<Int>()
                    val dateUpdated = singleSnapshot.child("dateUpdated").getValue<Date>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val trailer = singleSnapshot.child("trailer").getValue<String>()
                    val genreList = singleSnapshot.child("genre").getValue<ArrayList<String>>()
                    val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                    val status = singleSnapshot.child("status").getValue<Boolean>()
                    val video = singleSnapshot.child("video").getValue<String>()

                    if(UserLogin.info!!.id == seller!!){
                        val formatter = SimpleDateFormat("dd/MM/yyyy")
                        when(checkAdapter){
                            1->{
                                /*hidden film*/
                                if(formatter.format(dateUpdated!!).toString() == formatter.format(Date(0,0,1)).toString() && !status!!)
                                    newFilm.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!,status!!, video!! ))
                            }

                            2 ->{
                                /*current film*/
                                if(formatter.format(dateUpdated!!).toString() != formatter.format(Date(0,0,0)).toString() && status!!)
                                newFilm.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!,status!!, video!! ))

                            }
                            3->{
                                /*waiting film*/
                                if(formatter.format(dateUpdated!!).toString() == formatter.format(Date(0,0,0)).toString())
                                newFilm.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!,status!!, video!! ))
                            }
                        }
                    }
                }

                rcvListManagement!!.adapter = adapter

            }
            override fun onCancelled(error: DatabaseError) {}
        })

        adapter!!.onItemClick = { film ->
            val intent = Intent(this, SellerFilmDetailActivity::class.java)
            intent.putExtra("check", checkAdapter)
            intent.putExtra("Film", film)
            startActivity(intent)
        }

    }

    private fun InitViewComponent() {

        /*init view component*/
        rcvListManagement = findViewById(R.id.rcvListFilmMananagement)

    }

    private fun getIntentList(): Int {
        return intent.getIntExtra("currentCheck", 2)
    }

    private fun setSupportActionBarActivity(){
        checkAdapter = getIntentList()
        when {
            checkAdapter == 2 -> supportActionBar?.title = "Danh sách phim đang hiển thị"
            checkAdapter == 3 -> supportActionBar?.title = "Danh sách phim đang chờ duyệt"
            else -> supportActionBar?.title = "Danh sách phim đang ẩn"
        }

    }

}