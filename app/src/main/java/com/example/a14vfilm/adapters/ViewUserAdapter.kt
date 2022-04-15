package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.models.User
import com.example.a14vfilm.R

class ViewUserAdapter (private val userList: List<User>): RecyclerView.Adapter<ViewUserAdapter.ViewHolder>(),
    Filterable {

    var onItemClick: ((User) -> Unit)? = null
    var filterListResult: List<User> = userList

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewUserAdapter.ViewHolder, position: Int) {
        val user = filterListResult[position]

        holder.bindView(user)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(user)
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
            tbUserStatus.text = user.status
        }


    }

}