package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.JoinerTableAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.TransactionExtend
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class JoinerStatisticDetailActivity : AppCompatActivity() {

    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    /*init variable from view of activity*/
    private var tvJoinerName: TextView? = null
    private var tvJoinerDescription: TextView? = null
    private var tvJoinerDatePublished: TextView? = null
    private var tvJoinerTotal: TextView? = null

    private var ivJoinerImage: ImageView? = null

    private var rcvDetailJoiner: RecyclerView? = null

    /*init variable stored detail film*/
    private var filmDetail: Film? = null

    /*transaction*/
    private var transactionList: ArrayList<Transaction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joiner_statistic_detail)

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

                val adapter = JoinerTableAdapter(transactionList)
                rcvDetailJoiner!!.layoutManager = LinearLayoutManager(this@JoinerStatisticDetailActivity)
                rcvDetailJoiner!!.adapter = adapter

                tvJoinerTotal!!.text = "${transactionList.size} lượt thuê"
                tvJoinerName!!.text = filmDetail!!.name

                tvJoinerDescription!!.text = "Mô tả: " + filmDetail!!.description
                tvJoinerDatePublished!!.text = SimpleDateFormat("dd/MM/yyyy").format(filmDetail!!.datePublished).toString()
                Picasso.get().load(filmDetail!!.image).resize(400, 360).into(ivJoinerImage)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    /*init view component to variable*/
    private fun initViewComponent() {

        /*set view to variable*/
        tvJoinerName = findViewById(R.id.tvJoinerStatisticName)
        tvJoinerDescription = findViewById(R.id.tvJoinerStatisticDescription)
        tvJoinerDatePublished = findViewById(R.id.tvJoinerStatisticDatePublished)

        tvJoinerTotal = findViewById(R.id.tvJoinerStatisticJoinerCount)

        ivJoinerImage = findViewById(R.id.ivJoinerStatisticImage)
        ivJoinerImage!!.requestFocus()

        rcvDetailJoiner = findViewById(R.id.rcvJoinerList)

    }

    /*get data from previous activity*/
    private fun getDataIntent() {
        filmDetail = intent.getSerializableExtra("Film") as Film?
    }
}