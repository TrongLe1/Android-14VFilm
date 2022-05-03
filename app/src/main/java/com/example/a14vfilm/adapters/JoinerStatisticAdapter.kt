package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat


class JoinerStatisticAdapter (private val filmList: List<Film>): RecyclerView.Adapter<JoinerStatisticAdapter.ViewHolder>(), Filterable {

    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    var onItemClick: ((Film) -> Unit)? = null
    var rentCount: Int? = null
    var filterListResult: List<Film> = filmList

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JoinerStatisticAdapter.ViewHolder, position: Int) {

        val film = filterListResult[position]
        holder.bindView(film)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(film)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinerStatisticAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.items_seller_joiner_statistic,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){

        val tvName = listItemView.findViewById<TextView>(R.id.tvStatisticJoinerFilmName)!!
        val ivImage = listItemView.findViewById<ImageView>(R.id.ivSellerFilmImageItem)!!
        val tvDescription = listItemView.findViewById<TextView>(R.id.tvStatisticJoinerDescription)!!
        val tvDatePublished = listItemView.findViewById<TextView>(R.id.tvStatisticJoinerDatePublished)!!
        val tvTotal = listItemView.findViewById<TextView>(R.id.tvJoinerTotal)!!
        val btnViewJoiner = listItemView.findViewById<Button>(R.id.btnViewJoiners)!!

        fun bindView(film: Film) {
            val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("transaction")
            dbReference.addListenerForSingleValueEvent( object: ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    var count = 0
                    for (singleSnapshot in snapshot.children) {
                        val pname = singleSnapshot.child("film").getValue<String>()
                        if(pname.toString().equals(film.id))
                            count+=1
                    }
                    tvTotal.text = "Lượt thuê: $count lượt thuê"
                    tvName.text = film.name
                    Picasso.get().load(film.image).resize(130, 130).into(ivImage)
                    tvDescription.text = film.description

                    tvDatePublished.text = "Ngày đăng: " + SimpleDateFormat("dd/MM/yyyy").format(film.datePublished).toString()
                    btnViewJoiner!!.setOnClickListener {
                        onItemClick?.invoke(film)
                        rentCount = count
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        }

    }
}