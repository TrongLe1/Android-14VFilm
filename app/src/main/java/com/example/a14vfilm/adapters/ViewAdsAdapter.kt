package com.example.a14vfilm.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.ViewAdsDetailActivity
import com.example.a14vfilm.adminActivity.ViewGenreDetailActivity
import com.example.a14vfilm.models.Genre
import com.squareup.picasso.Picasso
import java.util.*

class ViewAdsAdapter (private val adsList: List<String>): RecyclerView.Adapter<ViewAdsAdapter.ViewHolder>(){

    var onItemClick: ((String) -> Unit)? = null
    var filterListResult: List<String> = adsList

    override fun onBindViewHolder(holder: ViewAdsAdapter.ViewHolder, position: Int) {
        val ads = filterListResult[position]

        holder.bindView(ads)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(ads)
            val intent = Intent(it.context, ViewAdsDetailActivity::class.java)
            intent.putExtra("ads", ads)
            it.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_view_ads,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }


    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val ivGenreImage = listItemView.findViewById<ImageView>(R.id.adapterviewads_IVFilm)

        fun bindView(ads: String) {
            //set image for genre
            if (ads != "")
                Picasso.get().load(ads).resize(375, 150).into(ivGenreImage)

        }

    }

}