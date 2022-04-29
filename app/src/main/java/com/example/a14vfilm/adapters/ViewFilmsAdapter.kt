package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Rating
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.models.User
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.ViewFilmDetailActivity
import com.example.a14vfilm.models.Film
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.coroutines.coroutineContext

class ViewFilmsAdapter (private val filmList: List<Film>): RecyclerView.Adapter<ViewFilmsAdapter.ViewHolder>(),
    Filterable {

    var onItemClick: ((Film) -> Unit)? = null
    var filterListResult: List<Film> = filmList

    override fun onBindViewHolder(holder: ViewFilmsAdapter.ViewHolder, position: Int) {
        val film = filterListResult[position]

        holder.bindView(film)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(film)
            val intent = Intent(it.context, ViewFilmDetailActivity::class.java)
            intent.putExtra("filmID", film.id)
            intent.putExtra("filmSeller", film.seller)
            intent.putExtra("filmName", film.name)
            intent.putExtra("filmDescription", film.description)
            intent.putExtra("filmRate", film.rate)
            intent.putExtra("filmLength", film.length)
            intent.putExtra("filmCountry", film.country)
            intent.putExtra("filmDatePublished", film.datePublished)
            intent.putExtra("filmPrice", film.price)
            //intent.putExtra("filmQuantity", film.quantity)
            intent.putExtra("filmDateUpdated", film.dateUpdated)
            intent.putExtra("filmImage", film.image)
            intent.putExtra("filmTrailer", film.trailer)
            intent.putExtra("filmGenre", film.genre.joinToString())
            intent.putExtra("filmRateCount", film.rateTime)
            it.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewFilmsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_view_film_admin,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterListResult = filmList
                } else {
                    val resultList = ArrayList<Film>()
                    for (row in filmList) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filterListResult = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterListResult
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterListResult = results?.values as ArrayList<Film>
                notifyDataSetChanged()
            }

        }
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvFilmName = listItemView.findViewById<TextView>(R.id.viewfilmadmin_tvFilmName)
        val rbFilmRate = listItemView.findViewById<RatingBar>(R.id.viewfilmadmin_rbFilmRate)
        val tvFilmDescription = listItemView.findViewById<TextView>(R.id.viewfilmadmin_tvFilmDescription)
        val tbFilmStatus = listItemView.findViewById<ToggleButton>(R.id.viewfilmadmin_btnStatus)
        val ivFilmImage = listItemView.findViewById<ImageView>(R.id.viewfilmadmin_imageView)

        fun bindView(film: Film) {
            tvFilmName.text = film.name
            rbFilmRate.rating = film.rate
            tvFilmDescription.text = film.description

            //set image for film
            if (film.image != "")
                Picasso.get().load(film.image).resize(150, 150).into(ivFilmImage)
            //set text for account status

//            if (film.status == "On"){
//                tbFilmStatus.isChecked = true
//            }
//            else{
//                tbFilmStatus.isChecked = false
//            }
//            //change status on database using the status button
//            tbFilmStatus.setOnClickListener{
//                //   Connect to firebase
//                val url = "https://demohw-9a24d-default-rtdb.firebaseio.com/"
//                val ref = FirebaseDatabase.getInstance(url).getReference("film")
//                var statusChange: String?
//
//                if (film.status == "On"){
//                    statusChange = "Off"
//                }
//                else{
//                    statusChange = "On"
//                }
//                ref.child(film.id)
//                    .child("status")
//                    .setValue(statusChange)
//            }

        }

    }



}