package com.example.a14vfilm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Genre

class GenreAdapter(private val genreList: List<Genre>): RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    var onItemClick: ((Genre) -> Unit)? = null
    var selectedPosition = 0
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), View.OnClickListener {
        val TVGenre = listItemView.findViewById<TextView>(R.id.TVGenre)
        init {
            listItemView.setOnClickListener { onItemClick?.invoke(genreList[adapterPosition]) }
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            notifyItemChanged(selectedPosition);
            selectedPosition = getAdapterPosition();
            notifyItemChanged(selectedPosition);
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_genre, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: GenreAdapter.ViewHolder, position: Int) {
        holder.TVGenre.text = genreList[position].name
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.BLUE)
            holder.TVGenre.setTextColor(Color.WHITE)
        }
        else {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            holder.TVGenre.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return genreList.size
    }
}