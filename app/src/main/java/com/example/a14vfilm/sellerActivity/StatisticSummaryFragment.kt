package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.key
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.RevenueStatisticAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.UserLogin
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatisticSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticSummaryFragment : Fragment() {

    private val MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24

    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    private var ref = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("film")

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /*Init variable of component view of fragment*/
    private lateinit var lineChart: LineChart
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private var totalFilm: TextView?= null
    private var totalRevenue: TextView?= null
    private var totalTransactionTextView: TextView?= null


    /*Init variable to data array display to Chart*/
//    private var

    /*array list stored transaction to statistic*/
    private var transactionList = ArrayList<Transaction>()
    private var filmList = ArrayList<Film>()
    private var filmIDList = ArrayList<String>()

    /*hashmap*/
    private var sevenDayRecentHashMap: SortedMap<Date, Long>? = null
//    private var totalTransaction = ArrayList<Transaction>()
//    private var totalUser = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistic_summary, container, false)

        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")

        totalFilm = view.findViewById(R.id.btnStatisticFilm)
        totalRevenue = view.findViewById(R.id.tvRevenue)
        totalTransactionTextView = view.findViewById(R.id.tvTotalTransaction)

        lineChart = view.findViewById(R.id.lineChart)
//        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)

        getAllSellerFilm()
        return view
    }

    private fun getAllTracsaction() {
        val newRef = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("transaction")
        newRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {

                val hashMap = HashMap<Date, Long>()
                var minus = 7
                while (minus > 0) {
                    val current = Date()
//                    hashMap[SimpleDateFormat("dd/MM/yyyy")
//                        .format(Date(current.time - minus * MILLIS_IN_A_DAY))
//                        .toString()] = 0;
                    hashMap[Date(current.time - minus * MILLIS_IN_A_DAY)] = 0
                    minus--
                }

                sevenDayRecentHashMap = hashMap.toSortedMap()
                val keyMap = ArrayList<String>()
                val valueMap = ArrayList<Long>()
                val joinerMap = ArrayList<Int>()
                for ((key, value) in sevenDayRecentHashMap!!) {
                    keyMap.add(SimpleDateFormat("dd/MM/yyyy")
                        .format(key).toString())
                    valueMap.add(value)
                    joinerMap.add(0)

//                    Log.e("Pppppp: ", "${SimpleDateFormat("dd/MM/yyyy")
//                        .format(key)} $value")

//                    sevenDayRecentHashMap!![key] = value
//                    keyMap.add(S/impleDateFormat("dd/MM/yyyy")
//                        .format(Date(key.time - minus * MILLIS_IN_A_DAY)).toString())
                }


                var totalMoney: Long = 0
                for (singleSnapshot in snapshot.children) {

                    val film = singleSnapshot.child("film").getValue<String>()
                    val id = singleSnapshot.child("id").getValue<String>()
                    val user = singleSnapshot.child("user").getValue<String>()
                    val rentDate = singleSnapshot.child("rentDate").getValue<Date>()
                    val expired = singleSnapshot.child("expired").getValue<Date>()
                    val total = singleSnapshot.child("total").getValue<Long>()
                    val rate = singleSnapshot.child("rate").getValue<Float>()
                    val type = singleSnapshot.child("type").getValue<Boolean>()
                    val comment = singleSnapshot.child("comment").getValue<String>()

                    if (filmIDList.contains(film)) {
                        transactionList.add(Transaction(id!!,
                            user!!,
                            film!!,
                            rentDate!!,
                            expired!!,
                            total!!,
                            rate!!,
                            type!!,
                            comment!!))

                        totalMoney += total!!
                        val date = SimpleDateFormat("dd/MM/yyyy").format(rentDate).toString()
//                        Log.e("ppppp", "$date")

                        if (keyMap.contains(date)) {
                            val idx = keyMap.indexOf(date)
                            valueMap[idx] += total
                            joinerMap[idx] += 1
//                            Log.e("ppppp", "$date $idx ${valueMap[idx]}")
//                            sevenDayRecentHashMap!![Date(date)] =
//                                sevenDayRecentHashMap!![Date(date)]?.plus(total!!)
//                            Log.e("pppppp", "$date ${sevenDayRecentHashMap!![date]} ")
                        }
                    }

                  
                }

                val formatter = DecimalFormat("#,###")
                totalRevenue!!.text = formatter.format(totalMoney)
                totalTransactionTextView!!.text = transactionList.size.toString()
                totalFilm!!.text = filmList.size.toString()

//                setDataToLineChart()
                val max = Collections.max(sevenDayRecentHashMap!!.values)
                val min = Collections.min(sevenDayRecentHashMap!!.values)

                val xValues = lineChart!!.xAxis
                xValues.setDrawGridLines(false)
                xValues.setDrawAxisLine(false)
                lineChart.axisRight.isEnabled = false
                lineChart.legend.isEnabled = false
                lineChart.description.isEnabled = false

                lineChart.animateX(1000, Easing.EaseInSine)

                xValues.setDrawLabels(true)
                xValues.granularity = 1f
//                xValues.labelRotationAngle = +90f

                val entries: ArrayList<Entry> = ArrayList()
                val labels: ArrayList<String> = ArrayList()

                var pos = 0
                while (pos <= 6) {
//                    Log.e("ppppp", "$pos ${valueMap[pos]}")
                    entries.add(Entry((pos + 1).toFloat(), valueMap[pos].toFloat()))
                    pos++
                }

                val lineDataSet = LineDataSet(entries, "")
                lineDataSet.label = labels.toString()
                lineDataSet.valueTextSize = 13f
                val data = LineData(lineDataSet)
                lineChart.data = data
                lineChart.invalidate()

                //set data to bar chart

                val xBarValues = barChart!!.xAxis
                xBarValues.setDrawGridLines(false)
                xBarValues.setDrawAxisLine(false)
                barChart.axisRight.isEnabled = false
                barChart.legend.isEnabled = false
                barChart.description.isEnabled = false

                barChart.animateX(1000, Easing.EaseInSine)

                xBarValues.setDrawLabels(true)
                xBarValues.granularity = 1f

                val barEntries = ArrayList<BarEntry>()
                val title: String = "Lượt thuê 7 ngày qua"

                pos = 0
                while (pos <= 6) {
                    Log.e("ppppp", "$pos ${joinerMap[pos]}")
                    barEntries.add(BarEntry((pos+1).toFloat(), joinerMap[pos].toFloat()))
                    pos++
                }

                val barDataSet = BarDataSet(barEntries, title)
                val barData = BarData(barDataSet)
                barDataSet.label = labels.toString()
                barDataSet.valueTextSize = 13f
                barChart.data = barData
                barChart.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getAllSellerFilm() {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val size = snapshot.childrenCount
                var count = 0
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

                    if (UserLogin.info!!.id == seller!!) {
                        filmIDList.add(id!!)
                        filmList.add(0,
                            Film(id!!,
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
                                video!!))
                    }
                    count++
                }

                totalFilm!!.text = filmList.size.toString()
////
                if (count == size.toInt()) {
                    getAllTracsaction()
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    private fun convertDate(d: String): String {
        val array = d.split("-")
        return array[2] + array[1] + array[0]
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticSummaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticSummaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}