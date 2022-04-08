package com.example.a14vfilm.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Transaction
import java.text.SimpleDateFormat

class TransactionAdapter(private val transList: List<Transaction>): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var context : Context? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val TVId = listItemView.findViewById<TextView>(R.id.TVTransId)
        val TVName = listItemView.findViewById<TextView>(R.id.TVNameTrans)
        val TVRentDate = listItemView.findViewById<TextView>(R.id.TVRentDate)
        val TVExpiredDate = listItemView.findViewById<TextView>(R.id.TVExpiredDate)
        val TVPrice = listItemView.findViewById<TextView>(R.id.TVPriceTrans)
        val BTNCancel = listItemView.findViewById<Button>(R.id.BTNCancel)
        val BTNRate = listItemView.findViewById<Button>(R.id.BTNRate)
        val RBTrans = listItemView.findViewById<RatingBar>(R.id.RBRateTrans)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_trans, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.TVId.text = "Hóa đơn: #"
        holder.TVName.text = transList[position].name
        holder.TVRentDate.text = "Ngày thuê: " + SimpleDateFormat("dd/MM/yyy").format(transList[position].rentDate)
        holder.TVExpiredDate.text = "Ngày hết hạn: " + SimpleDateFormat("dd/MM/yyy").format(transList[position].expired)
        holder.TVPrice.text = "Thành tiền: " + transList[position].total.toString() + " VNĐ"
        //Query
        if (true) {
            holder.BTNRate.setOnClickListener {
                val dialog = Dialog(context!!)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.dialog_rating)
                val RBDiaRate = dialog.findViewById<RatingBar>(R.id.RBDiaRate)
                val BTNDiaRate = dialog.findViewById<Button>(R.id.BTNDiaRate)
                BTNDiaRate.setOnClickListener {
                    dialog.dismiss()
                    //Query
                    holder.RBTrans.rating = RBDiaRate.rating
                    holder.BTNRate.isClickable = false
                    holder.BTNRate.isEnabled = false
                    holder.BTNRate.visibility = View.GONE
                }

                dialog.show()
            }
        }
        else {
            holder.BTNRate.isClickable = false
            holder.BTNRate.isEnabled = false
            holder.BTNRate.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }
}