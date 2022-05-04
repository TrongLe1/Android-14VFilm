package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.order.PlayVideoActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RevenueTableAdapter(private val joinerList: MutableList<Transaction>, private val renter: HashMap<String, Long?>) :
    RecyclerView.Adapter<RevenueTableAdapter.ViewHolder>() {

    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    var onItemClick: ((Transaction) -> Unit)? = null
    var joinerListResult: List<Transaction> = joinerList
    var renterList = renter.toSortedMap(compareByDescending { it })

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RevenueTableAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.items_revenue_table, parent, false))
    }

    override fun onBindViewHolder(holder: RevenueTableAdapter.ViewHolder, position: Int) {

        val transaction = joinerListResult[position]
        var count = 0
        for ((key, value) in renterList) {
            if(count == position) {
                holder.bindView(key, value!!)
            }
            count++
        }
//        holder.bindView(transaction)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(transaction)
        }

    }

    override fun getItemCount(): Int {
//        return joinerList.size
        return renterList.size
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val tvName = listItemView.findViewById<TextView>(R.id.tvJoinerTableName)
        val tvRentDate = listItemView.findViewById<TextView>(R.id.tvJoinerTableRentDate)
//        val tvExpiredDate = listItemView.findViewById<TextView>(R.id.tvJoinerTableExpiredDate)

        fun bindView(userid: String, revenue: Long?){

            val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                .getReference("user")
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {

                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (singleSnapshot in snapshot.children) {
                        val id = singleSnapshot.child("id").getValue<String>()
                        val name = singleSnapshot.child("name").getValue<String>()
                        val stringArray = name!!.trim().split(" ")
                        var resultName= ""
                        for(idx in stringArray.indices){
                            resultName += if(idx == stringArray.size - 1){
                                stringArray[idx]
                            }else
                                stringArray[idx][0] + "."
                        }
                        if (id.equals(userid))
                            tvName!!.text = resultName
                    }

                    val formatter = DecimalFormat("#,###")
                    tvRentDate!!.text = "${formatter.format(revenue!!)} VND"


//                    tvRentDate!!.text =
//                        SimpleDateFormat("dd/MM/yyyy").format(transaction.rentDate).toString()
//                    tvExpiredDate!!.text =
//                        SimpleDateFormat("dd/MM/yyyy").format(transaction.expired).toString()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
//
//        @SuppressLint("SimpleDateFormat")
//        fun bindView(transaction: Transaction) {
//
//            val dbReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
//                .getReference("user")
//            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    var hashMap: HashMap<String, Long> = HashMap()
//                    for (singleSnapshot in snapshot.children) {
//                        val id = singleSnapshot.child("id").getValue<String>()
//                        val name = singleSnapshot.child("name").getValue<String>()
//                        val stringArray = name!!.trim().split(" ")
//                        var resultName= ""
//                        for(idx in stringArray.indices){
//                            resultName += if(idx == stringArray.size - 1){
//                                stringArray[idx]
//                            }else
//                                stringArray[idx][0] + "."
//                        }
//                        if (id.equals(transaction.user))
//                            tvName!!.text = resultName
//                    }
//                    tvRentDate!!.text =
//                        SimpleDateFormat("dd/MM/yyyy").format(transaction.rentDate).toString()
////                    tvExpiredDate!!.text =
////                        SimpleDateFormat("dd/MM/yyyy").format(transaction.expired).toString()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })

//        }

    }

}