package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
    private var totalRevenue: Long? = null
    private var renterHashMap: HashMap<String, Long?> ? = HashMap()

    /*inti pie chart*/
    private var pcRevenue: PieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revenue_statistic_detail)

        /*Init*/
        initViewComponent()

        /*get data from previous activity*/
        getDataIntent()
        displayDataToViewComponent()

        /*set up piecharrt */
        setUpPieChart()
    }

    private fun setUpPieChart() {

        val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("user")
        dbReference.addListenerForSingleValueEvent( object: ValueEventListener {

            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                val a = renterHashMap!!.toSortedMap(compareByDescending { it })
                val result = HashMap<String, Long?>()
                for ((key, value) in a ){
                    var isExist = false
                    for (singleSnapshot in snapshot.children) {
                        val id = singleSnapshot.child("id").getValue<String>()
                        val name = singleSnapshot.child("name").getValue<String>()

                        if(id == key){
                            result[name!!] = value
                            isExist = true
                        }
                    }
                    if(!isExist)
                        result[" "] = value
                }

                val yValues = ArrayList<PieEntry>()
                val colors = ArrayList<Int>()

                var pos = 0
                var totalMoney: Long? = 0
                for ((key, value) in result ){

                    when(pos) {
                        0 -> colors.add(Color.RED)
                        1 -> colors.add(Color.BLUE)
                        2 -> colors.add(Color.YELLOW)
                        3 -> colors.add(Color.CYAN)
                        4 -> colors.add(Color.LTGRAY)
                    }

                    yValues.add(PieEntry(value!!/totalRevenue!!.toFloat(), key))
                    totalMoney = totalMoney?.plus(value!!)
                    pos++
                }

                if(pos == 5 && totalMoney!! < totalRevenue!!){
                    yValues.add(PieEntry((totalRevenue!! - totalMoney)!!/(totalRevenue)!!.toFloat(), "Khác"))

                    colors.add(Color.GREEN)
                }

                val piechartEntry = PieDataSet(yValues, "Danh sách người dùng")
                piechartEntry.valueTextSize = 17f
//                piechartEntry.valueFormatter = PercentFormatter()
                piechartEntry.colors = colors
                piechartEntry.valueTextColor = Color.BLACK

                val data = PieData(piechartEntry)

                pcRevenue!!.data = data
                pcRevenue!!.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


    /*display film detail stored in film variable to view*/
    @SuppressLint("SetTextI18n")
    private fun displayDataToViewComponent() {

        /*accessing to Firebase Database to count how many times rent film*/
        val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("transaction")
        dbReference.addListenerForSingleValueEvent( object: ValueEventListener {

            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                totalRevenue = 0
                for (singleSnapshot in snapshot.children) {
                    val film = singleSnapshot.child("film").getValue<String>()
                    if(film.toString() == filmDetail!!.id){

                        val id = singleSnapshot.child("id").getValue<String>()
                        val user = singleSnapshot.child("user").getValue<String>()
                        val rentDate = singleSnapshot.child("rentDate").getValue<Date>()
                        val expired = singleSnapshot.child("expired").getValue<Date>()
                        val total = singleSnapshot.child("total").getValue<Long>()
                        val rate = singleSnapshot.child("rate").getValue<Float>()
                        val type = singleSnapshot.child("type").getValue<Boolean>()
                        val comment = singleSnapshot.child("comment").getValue<String>()
                        /*check user attemp in hashmap*/
                        if(renterHashMap!!.containsKey(user!!)){
                            /*update value (total revenue of specific user)*/
                            renterHashMap!![user] = renterHashMap!![user]?.plus(total!!)
                        }else{
                            /*push key - value of user - total*/
                            renterHashMap!![user] = total
                        }

                        transactionList.add(Transaction(id!!,user!!,film!!,rentDate!!,expired!!,total!!,rate!!,type!!,comment!!))
                        totalRevenue = totalRevenue?.plus(total)
                    }
                }

                val adapter = RevenueTableAdapter(transactionList, renterHashMap!!)
                rcvDetailRevenue!!.layoutManager = LinearLayoutManager(this@RevenueStatisticDetailActivity)
                rcvDetailRevenue!!.adapter = adapter

                val formatter = DecimalFormat("#,###")
                tvRevenueTotal!!.text = "${formatter.format(totalRevenue)} VND"
                tvRevenueName!!.text = filmDetail!!.name
                tvRevenueDescription!!.text = "Mô tả: " + filmDetail!!.description
                tvRevenueDatePublished!!.text =  SimpleDateFormat("dd/MM/yyyy").format(filmDetail!!.datePublished).toString()
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

        pcRevenue = findViewById(R.id.pcRevenue)

    }

    /*get data from previous activity*/
    private fun getDataIntent() {
        filmDetail = intent.getSerializableExtra("Film") as Film?
    }
}