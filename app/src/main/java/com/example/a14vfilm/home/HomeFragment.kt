package com.example.a14vfilm.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
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
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")

        var newFilm = ArrayList<Film>()
        val RVNew = view.findViewById<RecyclerView>(R.id.RVNew)
        var newAdapter = FilmAdapter(newFilm)
        RVNew.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        RVNew.addItemDecoration(LayoutMarginDecoration(1, 20))
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
                    //val quantity = singleSnapshot.child("quantity").getValue<Int>()
                    val dateUpdated = singleSnapshot.child("dateUpdated").getValue<Date>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val trailer = singleSnapshot.child("trailer").getValue<String>()
                    val genreList = singleSnapshot.child("genre").getValue<ArrayList<String>>()
                    val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                    val status = singleSnapshot.child("status").getValue<Boolean>()
                    if (status == true)
                        newFilm.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!, status!!, ""))
                }
                newFilm.sortByDescending { it.dateUpdated }
                if (newFilm.size >= 5)
                    newFilm = newFilm.subList(0, 5).toList() as ArrayList<Film>
                else
                    newFilm = newFilm.subList(0, newFilm.size).toList() as ArrayList<Film>
                newAdapter = FilmAdapter(newFilm)
                newAdapter.onItemClick = {film ->
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra("Film", film)
                    startActivity(intent)
                }
                RVNew.adapter = newAdapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        var hotFilm = ArrayList<Film>()
        val RVHot = view.findViewById<RecyclerView>(R.id.RVHot)
        var hotAdapter = FilmAdapter(hotFilm)
        RVHot.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        RVHot.addItemDecoration(LayoutMarginDecoration(1, 20))
        val sQuery = ref.orderByChild("rate")
        sQuery.addListenerForSingleValueEvent(object: ValueEventListener {
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
                    //val quantity = singleSnapshot.child("quantity").getValue<Int>()
                    val dateUpdated = singleSnapshot.child("dateUpdated").getValue<Date>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val trailer = singleSnapshot.child("trailer").getValue<String>()
                    val genreList = singleSnapshot.child("genre").getValue<ArrayList<String>>()
                    val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                    val status = singleSnapshot.child("status").getValue<Boolean>()
                    if (status == true)
                        hotFilm.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!, status!!, ""))
                }
                if (hotFilm.size >= 5)
                    hotFilm = hotFilm.subList(0, 5).toList() as ArrayList<Film>
                else
                    hotFilm = hotFilm.subList(0, hotFilm.size).toList() as ArrayList<Film>
                hotAdapter = FilmAdapter(hotFilm)
                hotAdapter.onItemClick = {film ->
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra("Film", film)
                    startActivity(intent)
                }
                RVHot.adapter = hotAdapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        /*
        val RVHome = view.findViewById<RecyclerView>(R.id.RVHome)
        val filmList = ArrayList<Film>()
        var homeAdapter = FilmAdapter(filmList)
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
                    val rateTime = singleSnapshot.child("rateTime").getValue<Int>()
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, quantity!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!))
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
        //ACTVSearch.setAdapter(ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("B5314. Scream 2022 - Ti???ng Th??t 2022 2D25G (DTS-HD MA 7.1)"))) //Query m???ng T??N phim m???i (hot)
        ACTVSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.toString().equals("")) TVHome.text = "S???n ph???m m???i nh???t"
                else TVHome.text = "K???t qu??? t??m ki???m"
                homeAdapter.filter.filter(p0)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })


        */

        val ACTVSearch = view.findViewById<AutoCompleteTextView>(R.id.ACTVSearch)
        ACTVSearch.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }

        val ISHome = view.findViewById<ImageSlider>(R.id.ISHome)
        val slideModel = ArrayList<SlideModel>()
        val sRef = FirebaseDatabase.getInstance(url).getReference("ads")
        sRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val image = singleSnapshot.getValue<String>()
                    slideModel.add(SlideModel(image, ScaleTypes.CENTER_CROP))
                }
                ISHome.setImageList(slideModel)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
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