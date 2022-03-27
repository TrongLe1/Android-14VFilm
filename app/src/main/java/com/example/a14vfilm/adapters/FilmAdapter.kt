package com.example.a14vfilm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film

class FilmAdapter (private val filmList: List<Film>): RecyclerView.Adapter<FilmAdapter.ViewHolder>(), Filterable {
    var onItemClick: ((Film) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val TVName = listItemView.findViewById<TextView>(R.id.TVName)
        val TVQuantity = listItemView.findViewById<TextView>(R.id.TVQuantity)
        val TVRate = listItemView.findViewById<TextView>(R.id.TVRate)
        val TVPrice = listItemView.findViewById<TextView>(R.id.TVPrice)
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
        holder.TVQuantity.text = "Số lượng: " + filterListResult[position].quantity.toString()
        val rate = filterListResult[position].like * 1.0 / (filterListResult[position].like * 1.0 + filterListResult[position].dislike * 1.0) * 100.0
        holder.TVRate.text = String.format("%.1f", rate) + "/100"
        holder.TVPrice.text = filterListResult[position].price.toString() + " VNĐ/ngày"
        holder.TVPrice.setTextColor(Color.RED)
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