package com.example.a14vfilm.adminActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAChart
import com.github.mikephil.charting.charts.BarChart
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminStatisticsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var tvUserCount: TextView? = null
    private var tvFilmCount: TextView? = null
    private var tvGenreCount: TextView? = null
    private var tvVerifyCount: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_statistics, container, false)

        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference()

        //Count user
        var countUser = 0
        tvUserCount = view.findViewById(R.id.fragmentstatistics_textView1)
        ref.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    countUser++
                }
                tvUserCount!!.append(countUser.toString())

            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //Count film
        var countFilm = 0
        tvFilmCount = view.findViewById(R.id.fragmentstatistics_textView2)
        ref.child("film").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.child("dateUpdated").getValue<Date>() != Date(0,0,0)){
                        countFilm++
                    }
                }
                tvFilmCount!!.append(countFilm.toString())
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //Count genre
        var countGenre = 0
        tvGenreCount = view.findViewById(R.id.fragmentstatistics_textView3)
        ref.child("genre").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                        countGenre++
                }
                tvGenreCount!!.append(countGenre.toString())
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        //Count unverified
        var countVerify = 0
        tvVerifyCount = view.findViewById(R.id.fragmentstatistics_textView4)
        ref.child("film").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.child("dateUpdated").getValue<Date>() == Date(0,0,0)){
                        countVerify++
                    }
                }
                tvVerifyCount!!.append(countVerify.toString())
            }
            override fun onCancelled(error: DatabaseError) {}
        })



        //count last 7 day
        var day1 = 0
        var day2 = 0
        var day3 = 0
        var day4 = 0
        var day5 = 0
        var day6 = 0
        var day7 = 0

        ref.child("transaction").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val dateUpdated = singleSnapshot.child("rentDate").getValue<Date>()
                    if ( dateUpdated!!.after(getDaysAgo(1)) ){
                        day7++
                    }
                    else if ( dateUpdated!!.after(getDaysAgo(2)) && dateUpdated!!.before(getDaysAgo(1)) )
                    {
                        day6++
                    }
                    else if ( dateUpdated!!.after(getDaysAgo(3)) && dateUpdated!!.before(getDaysAgo(2)) )
                    {
                        day5++
                    }
                    else if ( dateUpdated!!.after(getDaysAgo(4)) && dateUpdated!!.before(getDaysAgo(3)) )
                    {
                        day4++
                    }
                    else if ( dateUpdated!!.after(getDaysAgo(5)) && dateUpdated!!.before(getDaysAgo(4)) )
                    {
                        day3++
                    }
                    else if ( dateUpdated!!.after(getDaysAgo(6)) && dateUpdated!!.before(getDaysAgo(5)) )
                    {
                        day2++
                    }
                    else if ( dateUpdated!!.after(getDaysAgo(7)) && dateUpdated!!.before(getDaysAgo(6)) )
                    {
                        day1++
                    }
                }

                val aaChartView:AAChartView = view.findViewById<AAChartView>(R.id.fragmentstatistics_barChart)
                //config chart
                val aaChartModel : AAChartModel = AAChartModel()
                    .chartType(AAChartType.Line)
                    .title("Các giao dịch")
                    .subtitle("Số lượng giao dịch trong 7 ngày qua")
                    .backgroundColor("#C4DDFF")
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("Giao dịch")
                            .data(arrayOf(day1, day2, day3, day4, day5, day6, day7)),
                    )
                    )

                aaChartView.aa_drawChartWithChartModel(aaChartModel)


            }
            override fun onCancelled(error: DatabaseError) {}
        })




        return view
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminManagementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }


}