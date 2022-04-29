package com.example.a14vfilm.sellerActivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.FilmSellerManagementAdapter
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SellerManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SellerManagementFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_seller_management, container, false)

        initComponent(view)
        val filmList = ArrayList<Film>()
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("film")
        val rcvCurrent = view.findViewById<RecyclerView>(R.id.rcvSellerCurrent)
        val rcvExpired = view.findViewById<RecyclerView>(R.id.rcvSellerExpired)
        var homeAdapter = FilmSellerManagementAdapter(filmList)

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
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!))
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!))
                    filmList.add(0, Film(id!!, seller!!, name!!, description!!, rate!!, length!!, country!!, datePublished!!, price!!, dateUpdated!!, image!!, trailer!!, genreList!!, rateTime!!))
                }
                rcvCurrent.adapter = homeAdapter
                rcvExpired.adapter = homeAdapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        rcvCurrent.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        rcvExpired.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)

        homeAdapter.onItemClick = {film ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra("Film", film)
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SellerManagementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SellerManagementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun moveToSellerUploadFilmActivity(){
        val intent = Intent(activity, SellerUploadFilmActivity::class.java)
        startActivity(intent)
    }

    private fun moveToCurrentListActivity(msg: String){
        val intent = Intent(activity, SellerFilmListActivity::class.java)
        intent.putExtra("currentCheck",msg)
        startActivity(intent)
    }

    private fun initComponent(view: View){
        val btnUploadFilm = view.findViewById<Button>(R.id.btnFilmUpload)
        val ibCurrent = view.findViewById<ImageButton>(R.id.ibCurrent)
        val tvCurrent = view.findViewById<TextView>(R.id.tvCurrentList)
        val ibExpired = view.findViewById<ImageButton>(R.id.ibExpired)
        val tvExpired = view.findViewById<TextView>(R.id.tvExpiredList)


        btnUploadFilm!!.setOnClickListener {
            moveToSellerUploadFilmActivity()
        }

        ibCurrent!!.setOnClickListener {
            moveToCurrentListActivity("true")
        }

        tvCurrent!!.setOnClickListener {
            moveToCurrentListActivity("true")
        }

        ibExpired!!.setOnClickListener {
            moveToCurrentListActivity("false")

        }

        tvExpired!!.setOnClickListener {
            moveToCurrentListActivity("false")
        }

    }

}