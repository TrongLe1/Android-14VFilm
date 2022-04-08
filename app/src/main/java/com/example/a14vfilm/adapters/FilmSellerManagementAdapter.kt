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

class FilmSellerManagementAdapter (private val filmList: List<Film>): RecyclerView.Adapter<FilmSellerManagementAdapter.ViewHolder>(), Filterable {

    var onItemClick: ((Film) -> Unit)? = null
    var filterListResult: List<Film> = filmList

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmSellerManagementAdapter.ViewHolder, position: Int) {
        val film = filterListResult[position]

        holder.bindView(film)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(film)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmSellerManagementAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_film_management,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvName = listItemView.findViewById<TextView>(R.id.tvFilmName)!!
        val tvPublished = listItemView.findViewById<TextView>(R.id.tvDatePublished)!!

        fun bindView(film: Film) {
            tvName.text = film.name
            tvPublished.text = film.datePublished.toString()
        }


    }

}