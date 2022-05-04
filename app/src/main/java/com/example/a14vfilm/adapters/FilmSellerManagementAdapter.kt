package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class FilmSellerManagementAdapter (private val filmList: List<Film>, private val check: Int): RecyclerView.Adapter<FilmSellerManagementAdapter.ViewHolder>(), Filterable {

    var onItemClick: ((Film) -> Unit)? = null
    var filterListResult: List<Film> = filmList
    var checkValue: Int = check
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmSellerManagementAdapter.ViewHolder, position: Int) {
        val film = filterListResult[position]
        holder.bindView(film)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(film)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmSellerManagementAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.items_seller_home_current_management,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){

        val tvName = listItemView.findViewById<TextView>(R.id.tvName)!!
        val tvImage = listItemView.findViewById<ImageView>(R.id.ivFilm)!!
        val tvPublished = listItemView.findViewById<TextView>(R.id.tvDatePublished)!!

        fun bindView(film: Film) {
            tvName.text = film.name
            Picasso.get().load(film.image).resize(130, 130).into(tvImage)
            if(SimpleDateFormat("dd/MM/yyyy").format(film.dateUpdated) != SimpleDateFormat("dd/MM/yyyy").format(
                    Date(0,0,0))
                && SimpleDateFormat("dd/MM/yyyy").format(film.dateUpdated) != SimpleDateFormat("dd/MM/yyyy").format(
                    Date(0,0,1)))
                tvPublished.text = "Ngày đăng: ${SimpleDateFormat("dd/MM/yyyy").format(film.dateUpdated)}"
        }


    }

}