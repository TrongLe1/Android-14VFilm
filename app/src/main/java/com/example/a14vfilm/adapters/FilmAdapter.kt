package com.example.a14vfilm.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import java.net.URL
import android.graphics.Bitmap
import com.squareup.picasso.Picasso


class FilmAdapter (private val filmList: List<Film>): RecyclerView.Adapter<FilmAdapter.ViewHolder>(), Filterable {
    var onItemClick: ((Film) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val TVName = listItemView.findViewById<TextView>(R.id.TVName)!!
        //val TVQuantity = listItemView.findViewById<TextView>(R.id.TVQuantity)
        //val TVRate = listItemView.findViewById<TextView>(R.id.TVRate)!!
        val RBRate = listItemView.findViewById<RatingBar>(R.id.RBRate)!!
        val TVPrice = listItemView.findViewById<TextView>(R.id.TVPrice)!!
        val IVFilm = listItemView.findViewById<ImageView>(R.id.IVFilm)!!
        init {
            listItemView.setOnClickListener { onItemClick?.invoke(filterListResult[adapterPosition]) }
        }
    }

    var filterListResult: List<Film>
    init {
        filterListResult = filmList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_film, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.TVName.text = filterListResult[position].name
        if (filterListResult[position].image != "")
            Picasso.get().load(filterListResult[position].image).resize(130, 130).into(holder.IVFilm)
        //holder.TVQuantity.text = "Số lượng: " + filterListResult[position].quantity.toString()
        //val rate = filterListResult[position].like * 1.0 / (filterListResult[position].like * 1.0 + filterListResult[position].dislike * 1.0) * 100.0
        //holder.TVRate.text = String.format("%.1f", rate) + "/100"
        holder.RBRate.rating = filterListResult[position].rate
        holder.TVPrice.text = filterListResult[position].price.toString() + " VNĐ/Ngày"
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty())
                    filterListResult = filmList
                else {
                    val resultList = ArrayList<Film>()
                    for (row in filmList)
                        if (row.name.toLowerCase().contains(charSearch.toLowerCase()))
                            resultList.add(row)
                    filterListResult = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filterListResult
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterListResult = p1!!.values as List<Film>
                notifyDataSetChanged()
            }

        }
    }
}