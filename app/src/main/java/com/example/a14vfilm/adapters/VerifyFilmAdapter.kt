package com.example.a14vfilm.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.example.a14vfilm.R
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.util.*

class VerifyFilmAdapter (private val filmList: List<Film>): RecyclerView.Adapter<VerifyFilmAdapter.ViewHolder>(),
    Filterable {

    var onItemClick: ((Film) -> Unit)? = null
    var filterListResult: List<Film> = filmList

    override fun onBindViewHolder(holder: VerifyFilmAdapter.ViewHolder, position: Int) {
        val film = filterListResult[position]

        holder.bindView(film)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(film)
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("Film", film)
            it.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerifyFilmAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_verify_film,parent,false))
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
        val tvFilmName = listItemView.findViewById<TextView>(R.id.adapterverifyfilm_tvFilmName)
        val rbFilmRate = listItemView.findViewById<RatingBar>(R.id.adapterverifyfilm_rbFilmRate)
        val tvFilmDescription = listItemView.findViewById<TextView>(R.id.adapterverifyfilm_tvFilmDescription)
        val tbFilmStatus = listItemView.findViewById<Button>(R.id.adapterverifyfilm_btnStatus)
        val ivFilmImage = listItemView.findViewById<ImageView>(R.id.adapterverifyfilm_imageView)

        fun bindView(film: Film) {
            tvFilmName.text = film.name
            rbFilmRate.rating = film.rate
            tvFilmDescription.text = film.description

            //set image for film
            if (film.image != "")
                Picasso.get().load(film.image).resize(150, 150).into(ivFilmImage)


            //change status on database using the status button
            tbFilmStatus.setOnClickListener{
                //   Connect to firebase
                val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                val ref = FirebaseDatabase.getInstance(url).getReference()


                //find user information by query firebase
                val query = ref.child("film").child(film.id)
                //set status
                query.child("status").setValue(true)
                query.child("dateUpdated").setValue(Calendar.getInstance().time)


            }
        }

    }



}