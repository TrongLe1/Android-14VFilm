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
        val film1 = Film(1, "1", "B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)", "", 10, 3, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 100000.0, 10, Date(2021, 3, 27))
        val film2 = Film(2, "1", "B5306. Turning Red 2022 - Gấu Đỏ Biến Hình 2D25G (DTS-HD MA 7.1)", "", 5, 2, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 89000.0, 8, Date(2021, 3, 27))
        val film3 = Film(3, "1", "B5299. Blacklight 2022 - Phi Vụ Đen 2D25G (DTS-HD MA 7.1)", "", 8, 1, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 129000.0, 4, Date(2021, 3, 27))
        val film4 = Film(4, "1", "4KUHD-789. Spider-Man No Way Home 2022 (TRUE-HD7.1 - DOLBY ATMOS)", "", 13, 3, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 290000.0, 23, Date(2021, 3, 27))
        var libraryAdapter: FilmAdapter /*= FilmAdapter(arrayListOf(film1, film2, film3, film4))
        RVLibrary.adapter = libraryAdapter
        libraryAdapter.onItemClick = {film ->
            //Xem chi tiết phim
        }
        */
        RVLibrary.layoutManager = GridLayoutManager(requireActivity(), 2)
        val RVGenre = view.findViewById<RecyclerView>(R.id.RVCategory)
        val genre1 = Genre(1, "Phim kinh dị", "")
        val genre2 = Genre(2, "Phim tình cảm", "")
        val genre3 = Genre(3, "Phim tâm lý", "")
        val genreAdapter = GenreAdapter(arrayListOf(genre1, genre2, genre3))
        RVGenre.adapter = genreAdapter
        RVGenre.layoutManager = LinearLayoutManager(requireActivity())
        RVGenre.setBackgroundColor(Color.LTGRAY)
        val option = arrayListOf("Mới nhất", "Giá tăng dần", "Giá giảm dần")
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