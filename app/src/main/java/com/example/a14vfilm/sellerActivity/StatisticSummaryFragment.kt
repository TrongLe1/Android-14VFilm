package com.example.a14vfilm.sellerActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a14vfilm.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis

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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /*Init variable of component view of fragment*/
    private lateinit var lineChart: LineChart
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    /*Init variable to data array display to Chart*/
//    private var

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

        lineChart = view.findViewById(R.id.lineChart)
        pieChart = view.findViewById(R.id.pieChart)

        initViewComponent()
        return view
    }

    private fun initViewComponent() {

        /*set up chart*/
        initLineChart()
        initPieChart()
        initBarChart()

        /*passing data to chart*/
        setDataToLineChart()
        setDataToPieChart()
        setDataToBarChart()

    }

    private fun setDataToBarChart() {
        TODO("Not yet implemented")
    }

    private fun setDataToPieChart() {
        TODO("Not yet implemented")
    }

    private fun setDataToLineChart() {
        TODO("Not yet implemented")
    }

    private fun initLineChart(){

//        /*hide grid lines*/
//        lineChart.axisLeft.setDrawGridLines(false)
//        val xAxis: XAxis = lineChart.xAxis
//        xAxis.setDrawGridLines(false)
//        xAxis.setDrawAxisLine(false)
//
//        //remove right y-axis
//        lineChart.axisRight.isEnabled = false
//
//        //remove legend
//        lineChart.legend.isEnabled = false
//
//
//        //remove description label
//        lineChart.description.isEnabled = false
//
//
//        //add animation
//        lineChart.animateX(1000, Easing.EaseInSine)
//
//        // to draw label on xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
//        xAxis.valueFormatter = MyAxisFormatter()
//        xAxis.setDrawLabels(true)
//        xAxis.granularity = 1f
//        xAxis.labelRotationAngle = +90f



    }

    private fun initPieChart(){

    }

    private fun initBarChart(){

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