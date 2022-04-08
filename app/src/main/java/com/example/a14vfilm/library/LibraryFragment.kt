package com.example.a14vfilm.library

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmAdapter
import com.example.a14vfilm.adapters.GenreAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Genre
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
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val filmList = ArrayList<Film>()
        val genreList = ArrayList<Genre>()
        val ref = FirebaseDatabase.getInstance(url)
        val RVLibrary = view.findViewById<RecyclerView>(R.id.RVResult)
        val libraryAdapter = FilmAdapter(filmList)
        RVLibrary.layoutManager = GridLayoutManager(requireActivity(), 2)
        RVLibrary.addItemDecoration(LayoutMarginDecoration(2, 15))
        libraryAdapter.onItemClick = {film ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("Film", film)
            startActivity(intent)
        }

        var lastGenre = ""
        val RVGenre = view.findViewById<RecyclerView>(R.id.RVCategory)
        val genreAdapter = GenreAdapter(genreList)
        val query = ref.getReference("genre").orderByChild("id")
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val id = singleSnapshot.child("id").getValue<String>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val name = singleSnapshot.child("name").getValue<String>()
                    genreList.add(Genre(id!!, name!!, image!!))
                }
                lastGenre = genreList[0].name
                RVGenre.adapter = genreAdapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        RVGenre.layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.HORIZONTAL, false)
        val option = arrayListOf("Giá tăng dần", "Giá giảm dần", "Hay nhất")
        val SSort = view.findViewById<Spinner>(R.id.SSort)
        val sAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, option)
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SSort.adapter = sAdapter
        genreAdapter.onItemClick = {genre ->
            lastGenre = genre.name
            filmList.clear()
            getFilterLibrary(lastGenre, SSort.selectedItem.toString(), RVLibrary, filmList, libraryAdapter)
        }
        SSort.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filmList.clear()
                getFilterLibrary(lastGenre, SSort.selectedItem.toString(), RVLibrary, filmList, libraryAdapter)
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

    private fun getFilterLibrary(genre: String, sort: String, RVLibrary: RecyclerView, filmList: ArrayList<Film>, libraryAdapter: FilmAdapter) {
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        if (sort == "Giá giảm dần" || sort == "Giá tăng dần") {
            val query = ref.orderByChild("price")
            query.addValueEventListener(object: ValueEventListener {
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
                        for (i in 0 until genreList!!.size) {
                            if (genreList[i] == genre) {
                                if (sort == "Giá tăng dần") {
                                    filmList.add(
                                        Film(
                                            id!!,
                                            seller!!,
                                            name!!,
                                            description!!,
                                            rate!!,
                                            length!!,
                                            country!!,
                                            datePublished!!,
                                            price!!,
                                            quantity!!,
                                            dateUpdated!!,
                                            image!!,
                                            trailer!!,
                                            genreList
                                        )
                                    )
                                }
                                if (sort == "Giá giảm dần") {
                                    filmList.add(0,
                                        Film(
                                            id!!,
                                            seller!!,
                                            name!!,
                                            description!!,
                                            rate!!,
                                            length!!,
                                            country!!,
                                            datePublished!!,
                                            price!!,
                                            quantity!!,
                                            dateUpdated!!,
                                            image!!,
                                            trailer!!,
                                            genreList
                                        )
                                    )
                                }
                            }
                        }
                    }
                    RVLibrary.adapter = libraryAdapter
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
        else {
            val query = ref.orderByChild("rate")
            query.addValueEventListener(object: ValueEventListener {
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
                        for (i in 0 until genreList!!.size) {
                            if (genreList[i] == genre) {
                                filmList.add(0,
                                    Film(
                                        id!!,
                                        seller!!,
                                        name!!,
                                        description!!,
                                        rate!!,
                                        length!!,
                                        country!!,
                                        datePublished!!,
                                        price!!,
                                        quantity!!,
                                        dateUpdated!!,
                                        image!!,
                                        trailer!!,
                                        genreList
                                    )
                                )
                            }
                        }
                    }
                    RVLibrary.adapter = libraryAdapter
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}