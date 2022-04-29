package com.example.a14vfilm.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.ViewGenreDetailActivity
import com.example.a14vfilm.models.Genre
import com.squareup.picasso.Picasso
import java.util.*

class ViewGenreAdapter (private val genreList: List<Genre>): RecyclerView.Adapter<ViewGenreAdapter.ViewHolder>(),
    Filterable {

    var onItemClick: ((Genre) -> Unit)? = null
    var filterListResult: List<Genre> = genreList

    override fun onBindViewHolder(holder: ViewGenreAdapter.ViewHolder, position: Int) {
        val genre = filterListResult[position]

        holder.bindView(genre)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(genre)
            val intent = Intent(it.context, ViewGenreDetailActivity::class.java)
            intent.putExtra("genreID", genre.id)
            intent.putExtra("genreName", genre.name)
            intent.putExtra("genreImage", genre.image)
            it.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewGenreAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_view_genre_admin,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterListResult = genreList
                } else {
                    val resultList = ArrayList<Genre>()
                    for (row in genreList) {
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
                filterListResult = results?.values as ArrayList<Genre>
                notifyDataSetChanged()
            }

        }
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvGenreName = listItemView.findViewById<TextView>(R.id.viewgenreadmin_tvGenreName)
        val ivGenreImage = listItemView.findViewById<ImageView>(R.id.viewgenreadmin_imageView)

        fun bindView(genre: Genre) {
            tvGenreName.text = genre.name

            //set image for genre
            if (genre.image != "")
                Picasso.get().load(genre.image).resize(150, 150).into(ivGenreImage)

        }

    }



}