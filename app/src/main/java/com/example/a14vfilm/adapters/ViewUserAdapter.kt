package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.models.User
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.ViewUserActivity
import com.example.a14vfilm.adminActivity.ViewUserDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.coroutines.coroutineContext

class ViewUserAdapter (private val userList: List<User>): RecyclerView.Adapter<ViewUserAdapter.ViewHolder>(),
    Filterable {

    var onItemClick: ((User) -> Unit)? = null
    var filterListResult: List<User> = userList

    override fun onBindViewHolder(holder: ViewUserAdapter.ViewHolder, position: Int) {
        val user = filterListResult[position]

        holder.bindView(user)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(user)
            val intent = Intent(it.context, ViewUserDetailActivity::class.java)
            intent.putExtra("userID", user.id)
            intent.putExtra("userName", user.name)
            intent.putExtra("userEmail", user.email)
            intent.putExtra("userPassword", user.password)
            intent.putExtra("userAddress", user.address)
            intent.putExtra("userPhone", user.phone)
            it.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewUserAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_view_user,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvUserName = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserName)!!
        val tvUserEmail = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserEmail)!!
        val tvUserPhone = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserPhone)!!
        val tbUserStatus = listItemView.findViewById<ToggleButton>(R.id.viewuseradapter_btnStatus)!!

        fun bindView(user: User) {
            tvUserName.text = user.name
            tvUserEmail.text = user.email
            tvUserPhone.text = user.phone

            //set text for account status

//            if (user.status == "On"){
//                tbUserStatus.isChecked = true
//            }
//            else{
//                tbUserStatus.isChecked = false
//            }
//            //change status on database using the status button
//            tbUserStatus.setOnClickListener{
//             //   Connect to firebase
//            val url = "https://demohw-9a24d-default-rtdb.firebaseio.com/"
//            val ref = FirebaseDatabase.getInstance(url).getReference("user")
//            var statusChange: String?
//
//            if (user.status == "On"){
//                statusChange = "Off"
//            }
//            else{
//                statusChange = "On"
//            }
//            ref.child(user.id)
//                .child("status")
//                .setValue(statusChange)
//            }

        }

    }



}