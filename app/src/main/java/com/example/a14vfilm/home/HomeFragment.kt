package com.example.a14vfilm.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val RVHome = view.findViewById<RecyclerView>(R.id.RVHome)
        val film1 = Film(1, "1", "Scream 2022 - Tiếng Thét 2022", "Mô tả", 3.5F, 120, "Mỹ", Date(), 100000, 1, Date())
        val film2 = Film(2, "1", "Turning Red 2022 - Gấu Đỏ Biến Hình", "Mô tả", 4.5F, 120, "Mỹ", Date(), 99000, 2, Date())
        val film3 = Film(3, "1", "Blacklight 2022 - Phi Vụ Đen", "Mô tả", 4F, 120, "Mỹ", Date(), 90000, 10, Date())
        val film4 = Film(4, "1", "Spider-Man No Way Home 2022", "Mô tả", 5F, 120, "Mỹ", Date(), 89000, 4, Date())
        var homeAdapter = FilmAdapter(arrayListOf(film1, film2, film3, film4)) //Query mảng phim mới nhất
        RVHome.adapter = homeAdapter
        RVHome.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        homeAdapter.onItemClick = {film ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(film.name, "test")
            startActivity(intent)
        }
        RVHome.addItemDecoration(LayoutMarginDecoration(1, 20))
        /*
        val BTNNew = view.findViewById<Button>(R.id.BTNNew)
        BTNNew.setBackgroundColor(Color.BLUE)
        BTNNew.setTextColor(Color.WHITE)
        val BTNHot = view.findViewById<Button>(R.id.BTNHot)
        BTNHot.setBackgroundColor(Color.TRANSPARENT)
        BTNHot.setTextColor(Color.BLUE)
        */
        val TVHome = view.findViewById<TextView>(R.id.TVHome)
        val ACTVSearch = view.findViewById<AutoCompleteTextView>(R.id.ACTVSearch)
        //ACTVSearch.setAdapter(ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)"))) //Query mảng TÊN phim mới (hot)
        ACTVSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.toString().equals("")) TVHome.text = "Sản phẩm mới nhất"
                else TVHome.text = "Kết quả tìm kiếm"
                homeAdapter.filter.filter(p0)
                /*
                BTNHot.setBackgroundColor(Color.TRANSPARENT)
                BTNHot.setTextColor(Color.BLUE)
                BTNNew.setBackgroundColor(Color.TRANSPARENT)
                BTNNew.setTextColor(Color.BLUE)

                 */
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        /*
        BTNHot.setOnClickListener {
            ACTVSearch.text.clear()
            BTNHot.setBackgroundColor(Color.BLUE)
            BTNHot.setTextColor(Color.WHITE)
            BTNNew.setBackgroundColor(Color.TRANSPARENT)
            BTNNew.setTextColor(Color.BLUE)
            homeAdapter = FilmAdapter(arrayListOf(film4, film3, film2, film1)) //Query mảng phim hot
            RVHome.adapter = homeAdapter
        }
        BTNNew.setOnClickListener {
            ACTVSearch.text.clear()
            BTNNew.setBackgroundColor(Color.BLUE)
            BTNNew.setTextColor(Color.WHITE)
            BTNHot.setBackgroundColor(Color.TRANSPARENT)
            BTNHot.setTextColor(Color.BLUE)
            homeAdapter = FilmAdapter(arrayListOf(film1, film2, film3, film4)) //Query mảng phim mới nhất
            RVHome.adapter = homeAdapter
        }

         */
        val ISHome = view.findViewById<ImageSlider>(R.id.ISHome)
        val slideModel = ArrayList<SlideModel>()
        slideModel.add(SlideModel("https://teaser-trailer.com/wp-content/uploads/Avengers-Infinity-War-Banner.jpg", ScaleTypes.CENTER_CROP))
        slideModel.add(SlideModel("https://collider.com/wp-content/uploads/inception_movie_poster_banner_03.jpg", ScaleTypes.CENTER_CROP))
        slideModel.add(SlideModel("http://images6.fanpop.com/image/photos/40000000/The-Finest-Hours-Banner-movie-trailers-40025062-1200-638.jpg", ScaleTypes.CENTER_CROP))
        ISHome.setImageList(slideModel)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}