package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.RevenueTableAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Transaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class RevenueStatisticDetailActivity : AppCompatActivity() {
    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    /*init variable from view of activity*/
    private var tvRevenueName: TextView? = null
    private var tvRevenueDescription: TextView? = null
    private var tvRevenueDatePublished: TextView? = null
    private var tvRevenueTotal: TextView? = null

    private var ivRevenueImage: ImageView? = null

    private var rcvDetailRevenue: RecyclerView? = null

    /*init variable stored detail film*/
    private var filmDetail: Film? = null

    /*transaction*/
    private var transactionList: ArrayList<Transaction> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revenue_statistic_detail)

        /*Init*/
        initViewComponent()

        /*get data from previous activity*/
        getDataIntent()
        displayDataToViewComponent()

    }



    /*display film detail stored in film variable to view*/
    @SuppressLint("SetTextI18n")
    private fun displayDataToViewComponent() {

        /*accessing to Firebase Database to count how many times rent film*/
        val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("transaction")
        dbReference.addListenerForSingleValueEvent( object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val film = singleSnapshot.child("film").getValue<String>()
                    if(film.toString().equals(filmDetail!!.id)){
                        val id = singleSnapshot.child("id").getValue<String>()
                        val user = singleSnapshot.child("user").getValue<String>()
                        val rentDate = singleSnapshot.child("rentDate").getValue<Date>()
                        val expired = singleSnapshot.child("expired").getValue<Date>()
                        val total = singleSnapshot.child("total").getValue<Long>()
                        val rate = singleSnapshot.child("rate").getValue<Float>()
                        val type = singleSnapshot.child("type").getValue<Boolean>()
                        val comment = singleSnapshot.child("comment").getValue<String>()
                        transactionList.add(Transaction(id!!,user!!,film!!,rentDate!!,expired!!,total!!,rate!!,type!!,comment!!))
                    }
                }

                val adapter = RevenueTableAdapter(transactionList)
                rcvDetailRevenue!!.layoutManager = LinearLayoutManager(this@RevenueStatisticDetailActivity)
                rcvDetailRevenue!!.adapter = adapter

                tvRevenueTotal!!.text = "${transactionList.size} lượt thuê"
                tvRevenueName!!.text = filmDetail!!.name
                tvRevenueDescription!!.text = "Mô tả: " + filmDetail!!.description
                tvRevenueDatePublished!!.text = filmDetail!!.datePublished.toString()
                Picasso.get().load(filmDetail!!.image).resize(400, 360).into(ivRevenueImage)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    /*init view component to variable*/
    private fun initViewComponent() {

        /*set view to variable*/
        tvRevenueName = findViewById(R.id.tvRevenueStatisticName)
        tvRevenueDescription = findViewById(R.id.tvRevenueStatisticDescription)
        tvRevenueDatePublished = findViewById(R.id.tvRevenueStatisticDatePublished)

        tvRevenueTotal = findViewById(R.id.tvRevenueStatisticRevenueCount)

        ivRevenueImage = findViewById(R.id.ivRevenueStatisticImage)
        ivRevenueImage!!.requestFocus()

        rcvDetailRevenue = findViewById(R.id.rcvRevenueList)

    }

    /*get data from previous activity*/
    private fun getDataIntent() {
        filmDetail = intent.getSerializableExtra("Film") as Film?
    }
}