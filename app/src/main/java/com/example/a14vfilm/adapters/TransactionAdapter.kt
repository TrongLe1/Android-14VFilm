package com.example.a14vfilm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Transaction

class TransactionAdapter(private val transList: List<Transaction>): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val TVName = listItemView.findViewById<TextView>(R.id.TVNameTrans)
        val TVRentDate = listItemView.findViewById<TextView>(R.id.TVRentDate)
        val TVExpiredDate = listItemView.findViewById<TextView>(R.id.TVExpiredDate)
        val TVPrice = listItemView.findViewById<TextView>(R.id.TVPriceTrans)
        val BTNLike = listItemView.findViewById<Button>(R.id.BTNLike)
        val BTNDislike = listItemView.findViewById<Button>(R.id.BTNDislike)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_trans, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.TVName.text = transList[position].name
        holder.TVRentDate.text = "Ngày thuê: " + transList[position].rentDate.day
        holder.TVExpiredDate.text = "Ngày hết hạn: " + transList[position].expired.day
        holder.TVPrice.text = "Thành tiền: " + transList[position].total.toString() + " VNĐ"
        holder.TVPrice.setTextColor(Color.RED)
        holder.BTNLike.setBackgroundColor(Color.BLUE)
        holder.BTNLike.setTextColor(Color.WHITE)
        holder.BTNDislike.setBackgroundColor(Color.RED)
        holder.BTNDislike.setTextColor(Color.DKGRAY)
        if (transList[position].total == 10000.0) {
            holder.BTNDislike.isEnabled = false
            holder.BTNDislike.visibility = View.GONE
            holder.BTNLike.text = "Đã đánh giá"
            holder.BTNLike.setBackgroundColor(Color.DKGRAY)
            holder.BTNLike.isEnabled = false
        }
        else {
            holder.BTNLike.setOnClickListener {
                //Đánh giá thích
            }
            holder.BTNDislike.setOnClickListener {
                //Đánh giá không thích
            }
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }
}