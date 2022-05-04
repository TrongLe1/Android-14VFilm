package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.sellerActivity.SellerFilmDetailActivity
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class CurrentSellerAdapter (private val filmList: List<Film>, private val check: Int): RecyclerView.Adapter<CurrentSellerAdapter.ViewHolder>(), Filterable {

    var onItemClick: ((Film) -> Unit)? = null
    var filterListResult: List<Film> = filmList
    var checkValue: Int = check

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CurrentSellerAdapter.ViewHolder, position: Int) {
        val film = filterListResult[position]
        holder.bindView(film)
//        holder.itemView.setOnClickListener{
//            onItemClick?.invoke(film)
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentSellerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.items_seller_film_management,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }


    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){

        val tvName = listItemView.findViewById<TextView>(R.id.tvSellerFilmNameItem)
        val tvPublished = listItemView.findViewById<TextView>(R.id.tvSellerFilmDatePublishedItem)
        val tvCountry = listItemView.findViewById<TextView>(R.id.tvSellerFilmCountry)
        val tvDescription = listItemView.findViewById<TextView>(R.id.tvSellerFilmDescriptionItem)
        val tvImage = listItemView.findViewById<ImageView>(R.id.ivSellerFilmImageItem)
        val btnDetail = listItemView.findViewById<Button>(R.id.btnItemDetail)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindView(film: Film) {

            tvName.text = film.name
            if(checkValue == 2)
                tvPublished.text = "Ngày đăng: ${SimpleDateFormat("dd/MM/yyyy").format(film.dateUpdated)}"
            tvCountry.text = "Quốc gia: ${film.country}"
            tvDescription.text = film.description
            Picasso.get().load(film.image).resize(130, 130).into(tvImage)
            btnDetail.setOnClickListener {
                onItemClick?.invoke(film)
            }

            tvImage.setOnClickListener{
                onItemClick?.invoke(film)
            }

        }


    }

}