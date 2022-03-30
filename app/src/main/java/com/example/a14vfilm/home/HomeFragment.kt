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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import java.util.*

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
        val film1 = Film(1, "1", "B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)", "", 10, 3, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 100000.0, 10, Date(2021, 3, 27))
        val film2 = Film(2, "1", "B5306. Turning Red 2022 - Gấu Đỏ Biến Hình 2D25G (DTS-HD MA 7.1)", "", 5, 2, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 89000.0, 8, Date(2021, 3, 27))
        val film3 = Film(3, "1", "B5299. Blacklight 2022 - Phi Vụ Đen 2D25G (DTS-HD MA 7.1)", "", 8, 1, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 129000.0, 4, Date(2021, 3, 27))
        val film4 = Film(4, "1", "4KUHD-789. Spider-Man No Way Home 2022 (TRUE-HD7.1 - DOLBY ATMOS)", "", 13, 3, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 290000.0, 23, Date(2021, 3, 27))
        var homeAdapter = FilmAdapter(arrayListOf(film1, film2, film3, film4)) //Query mảng phim mới nhất
        RVHome.adapter = homeAdapter
        RVHome.layoutManager = GridLayoutManager(requireActivity(), 2)
        homeAdapter.onItemClick = {film ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            startActivity(intent)
        }
        val BTNNew = view.findViewById<Button>(R.id.BTNNew)
        BTNNew.setBackgroundColor(Color.BLUE)
        BTNNew.setTextColor(Color.WHITE)
        val BTNHot = view.findViewById<Button>(R.id.BTNHot)
        BTNHot.setBackgroundColor(Color.TRANSPARENT)
        BTNHot.setTextColor(Color.BLUE)
        val ACTVSearch = view.findViewById<AutoCompleteTextView>(R.id.ACTVSearch)
        ACTVSearch.setAdapter(ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)"))) //Query mảng TÊN phim mới (hot)
        ACTVSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                homeAdapter.filter.filter(p0)
                BTNHot.setBackgroundColor(Color.TRANSPARENT)
                BTNHot.setTextColor(Color.BLUE)
                BTNNew.setBackgroundColor(Color.TRANSPARENT)
                BTNNew.setTextColor(Color.BLUE)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
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