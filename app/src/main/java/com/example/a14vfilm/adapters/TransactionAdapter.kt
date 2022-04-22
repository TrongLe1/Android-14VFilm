package com.example.a14vfilm.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.TransactionExtend
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class TransactionAdapter(private val transList: MutableList<TransactionExtend>, private val type: String): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var context : Context? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val IVTrans = listItemView.findViewById<ImageView>(R.id.IVTrans)!!
        val TVId = listItemView.findViewById<TextView>(R.id.TVTransId)!!
        val TVName = listItemView.findViewById<TextView>(R.id.TVNameTrans)!!
        val TVRentDate = listItemView.findViewById<TextView>(R.id.TVRentDate)!!
        val TVExpiredDate = listItemView.findViewById<TextView>(R.id.TVExpiredDate)!!
        val TVPrice = listItemView.findViewById<TextView>(R.id.TVPriceTrans)!!
        val BTNCancel = listItemView.findViewById<Button>(R.id.BTNCancel)!!
        val BTNRate = listItemView.findViewById<Button>(R.id.BTNRate)!!
        val RBTrans = listItemView.findViewById<RatingBar>(R.id.RBRateTrans)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_trans, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == "ordered") {
            /*
            holder.BTNRate.visibility = View.GONE
            holder.RBTrans.visibility = View.GONE

            */
            holder.BTNCancel.visibility = View.GONE

        }
        /*
        else if (type == "expired")
            holder.BTNCancel.visibility = View.GONE

        */
        else {
            holder.BTNRate.visibility = View.GONE
            holder.RBTrans.visibility = View.GONE
            holder.BTNCancel.visibility = View.GONE
        }


        if (transList[position].image != "")
            Picasso.get().load(transList[position].image).resize(130, 130).into(holder.IVTrans)
        holder.TVId.text = "Hóa đơn: #" + transList[position].transaction.id
        holder.TVName.text = transList[position].name
        holder.TVRentDate.text = "Ngày thuê: " + SimpleDateFormat("dd/MM/yyy").format(transList[position].transaction.rentDate)
        holder.TVExpiredDate.text = "Ngày hết hạn: " + SimpleDateFormat("dd/MM/yyy").format(transList[position].transaction.expired)
        holder.TVPrice.text = "Thành tiền: " + transList[position].transaction.total.toString() + " VNĐ"
        if (transList[position].transaction.rate == -1F) {
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
                    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                    val ref = FirebaseDatabase.getInstance(url).getReference("transaction")
                    ref.child(transList[position].transaction.id).child("rate").setValue(RBDiaRate.rating)
                    val query = ref.orderByChild("film").equalTo(transList[position].transaction.film)
                    query.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var count = 0
                            var total = 0F
                            for (singleSnapshot in snapshot.children) {
                                val rate = singleSnapshot.child("rate").getValue<Float>()
                                if (rate != -1F) {
                                    count++
                                    total += rate!!
                                }
                            }
                            val totalRate = total / count
                            val sRef = FirebaseDatabase.getInstance(url).getReference("film")
                            sRef.child(transList[position].transaction.film).child("rate").setValue(totalRate)
                            sRef.child(transList[position].transaction.film).child("rateTime").setValue(count)
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                    holder.RBTrans.rating = RBDiaRate.rating
                    holder.BTNRate.isClickable = false
                    holder.BTNRate.isEnabled = false
                    holder.BTNRate.visibility = View.GONE
                }
                dialog.show()
            }
        }
        else {
            holder.RBTrans.rating = transList[position].transaction.rate
            holder.BTNRate.isClickable = false
            holder.BTNRate.isEnabled = false
            holder.BTNRate.visibility = View.GONE
        }
        holder.BTNCancel.setOnClickListener {
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference("transaction")
            ref.child(transList[position].transaction.id).child("type").setValue(false)

            /*
            val sRef = FirebaseDatabase.getInstance(url).getReference("film")
            val query = sRef.orderByChild("id").equalTo(transList[position].transaction.film)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        val quantity = singleSnapshot.child("quantity").getValue<Int>()
                        singleSnapshot.ref.child("quantity").setValue(quantity!! + 1)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            */
            transList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, transList.size)
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }
}