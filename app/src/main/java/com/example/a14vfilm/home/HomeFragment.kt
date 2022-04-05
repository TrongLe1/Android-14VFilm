package com.example.a14vfilm.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.a14vfilm.models.Genre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
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
        val filmList = ArrayList<Film>()
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        val RVHome = view.findViewById<RecyclerView>(R.id.RVHome)
        var homeAdapter = FilmAdapter(filmList)

        /*
        val genre = ArrayList<String>()
        genre.add("Tình cảm")
        genre.add("Kinh dị")
        val film1 = Film(ref.push().key!!, "1", "Scream 2022 - Tiếng Thét 2022", "Mô tả", 2.5F, 120, "Mỹ", Date(), 120000, 1, Date(), "", "", genre)
        ref.child(film1.id).setValue(film1)
        */

        val query = ref.orderByChild("dateUpdated")
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
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
                    val quantity = singleSnapshot.child("quantity").getValue<Int>()
                    val dateUpdated = singleSnapshot.child("dateUpdated").getValue<Date>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val trailer = singleSnapshot.child("trailer").getValue<String>()
                    val genreList = singleSnapshot.child("genre").getValue<ArrayList<String>>()
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, quantity!!, dateUpdated!!, image!!, trailer!!, genreList!!))
                }
                RVHome.adapter = homeAdapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        RVHome.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        homeAdapter.onItemClick = {film ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("Film", film)
            startActivity(intent)
        }
        RVHome.addItemDecoration(LayoutMarginDecoration(1, 20))
        val TVHome = view.findViewById<TextView>(R.id.TVHome)
        val ACTVSearch = view.findViewById<AutoCompleteTextView>(R.id.ACTVSearch)
        //ACTVSearch.setAdapter(ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)"))) //Query mảng TÊN phim mới (hot)
        ACTVSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.toString().equals("")) TVHome.text = "Sản phẩm mới nhất"
                else TVHome.text = "Kết quả tìm kiếm"
                homeAdapter.filter.filter(p0)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
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