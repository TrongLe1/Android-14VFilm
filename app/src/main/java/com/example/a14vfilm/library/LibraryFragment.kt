package com.example.a14vfilm.library

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmAdapter
import com.example.a14vfilm.adapters.GenreAdapter
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Genre
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LibraryFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        val RVLibrary = view.findViewById<RecyclerView>(R.id.RVResult)
        val film1 = Film(1, "1", "Scream 2022 - Tiếng Thét 2022", "Mô tả", 3.5F, 120, "Mỹ", Date(), 100000, 1, Date())
        val film2 = Film(2, "1", "Turning Red 2022 - Gấu Đỏ Biến Hình", "Mô tả", 4.5F, 120, "Mỹ", Date(), 99000, 2, Date())
        val film3 = Film(3, "1", "Blacklight 2022 - Phi Vụ Đen", "Mô tả", 4F, 120, "Mỹ", Date(), 90000, 10, Date())
        val film4 = Film(4, "1", "Spider-Man No Way Home 2022", "Mô tả", 5F, 120, "Mỹ", Date(), 89000, 4, Date())
        var libraryAdapter: FilmAdapter /*= FilmAdapter(arrayListOf(film1, film2, film3, film4))
        RVLibrary.adapter = libraryAdapter
        libraryAdapter.onItemClick = {film ->
            //Xem chi tiết phim
        }
        */
        RVLibrary.layoutManager = GridLayoutManager(requireActivity(), 2)
        RVLibrary.addItemDecoration(LayoutMarginDecoration(2, 15))
        val RVGenre = view.findViewById<RecyclerView>(R.id.RVCategory)
        val genre1 = Genre(1, "Phim kinh dị", "")
        val genre2 = Genre(2, "Phim tình cảm", "")
        val genre3 = Genre(3, "Phim tâm lý", "")
        val genreAdapter = GenreAdapter(arrayListOf(genre1, genre2, genre3))
        RVGenre.adapter = genreAdapter
        RVGenre.layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.HORIZONTAL, false)
        val option = arrayListOf("Giá tăng dần", "Giá giảm dần", "Tốt nhất")
        val SSort = view.findViewById<Spinner>(R.id.SSort)
        val sAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, option)
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SSort.adapter = sAdapter
        var lastGenre = "Phim kinh dị"
        genreAdapter.onItemClick = {genre ->
            lastGenre = genre.name //Lưu tên genre vào lastGenre
            SSort.selectedItem.toString()
            //Query mảng phim theo thể loại và sắp xếp
            libraryAdapter = FilmAdapter(arrayListOf(film2, film1, film4, film3))
            RVLibrary.adapter = libraryAdapter
        }
        SSort.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                SSort.selectedItem.toString()
                //Query mảng phim theo thể loại và sắp xếp
                libraryAdapter = FilmAdapter(arrayListOf(film3, film4, film1, film2))
                RVLibrary.adapter = libraryAdapter
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LibraryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}