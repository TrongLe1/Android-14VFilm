package com.example.a14vfilm.adapters

import android.media.Rating
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.FavoriteExtend
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso

class FavoriteAdapter (private val filmList: MutableList<FavoriteExtend>): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var onItemClick: ((Film) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val IVFavorite = listItemView.findViewById<ImageView>(R.id.IVFavorite)!!
        val TVFavorite = listItemView.findViewById<TextView>(R.id.TVFavorite)!!
        val RBFavorite = listItemView.findViewById<RatingBar>(R.id.RBFavorite)!!
        val TVFavPrice = listItemView.findViewById<TextView>(R.id.TVFavPrice)!!
        val IBFavorite = listItemView.findViewById<ImageButton>(R.id.IBFav)!!
        init {
            listItemView.setOnClickListener { onItemClick?.invoke(filmList[adapterPosition].film) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_favorite, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (filmList[position].film.image != "")
            Picasso.get().load(filmList[position].film.image).resize(130, 130).into(holder.IVFavorite)
        holder.TVFavorite.text = filmList[position].film.name
        holder.RBFavorite.rating = filmList[position].film.rate
        holder.TVFavPrice.text = filmList[position].film.price.toString() + " VNĐ/Ngày"
        holder.IBFavorite.setOnClickListener {
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference("favorite")
            val query = ref.orderByChild("id").equalTo(filmList[position].favorite.id)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        singleSnapshot.ref.removeValue()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            filmList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, filmList.size)
        }
    }
}