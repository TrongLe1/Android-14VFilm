package com.example.a14vfilm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film

class DetailAdapter(private val detail: List<Film>): RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val TVName = listItemView.findViewById<TextView>(R.id.TVDName)
        val TVType = listItemView.findViewById<TextView>(R.id.TVDType)
        val TVLength = listItemView.findViewById<TextView>(R.id.TVDLength)
        val TVCountry = listItemView.findViewById<TextView>(R.id.TVDCountry)
        val TVDate = listItemView.findViewById<TextView>(R.id.TVDDPublished)
        val TVPrice = listItemView.findViewById<TextView>(R.id.TVDPrice)
        val TVQuantity = listItemView.findViewById<TextView>(R.id.TVDQuantity)
        val TVDescription = listItemView.findViewById<TextView>(R.id.TVDDescription)
        val TVRate = listItemView.findViewById<TextView>(R.id.TVDRate)
        val TVGenre = listItemView.findViewById<TextView>(R.id.TVDGenre)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_detail, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: DetailAdapter.ViewHolder, position: Int) {
        holder.TVName.text = detail[position].name
        holder.TVCountry.text = "Nước sản xuất: " + detail[position].country
        holder.TVDate.text = "Ngày công chiếu: " + detail[position].datePublished.day
        holder.TVDescription.text = detail[position].description
        holder.TVType.text = "Loại: " + detail[position].type
        holder.TVLength.text = "Thời lượng: " + detail[position].length.toString() + " phút"
        holder.TVPrice.text = "Giá thuê: " + detail[position].price + " VNĐ/ngày"
        holder.TVQuantity.text = "Số lượng: " + detail[position].quantity
        val rate = detail[position].like * 1.0 / (detail[position].like * 1.0 + detail[position].dislike * 1.0) * 100.0
        holder.TVRate.text = "Đánh giá: " + String.format("%.1f", rate) + "/100"
        holder.TVGenre.text = "Thể loại: "
        holder.TVPrice.setTextColor(Color.RED)
    }

    override fun getItemCount(): Int {
        return detail.size
    }

}