package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.UserLogin
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
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
            intent.putExtra("userImage", user.image)
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
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterListResult = userList
                } else {
                    val resultList = ArrayList<User>()
                    for (row in userList) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filterListResult = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterListResult
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterListResult = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }

        }
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvUserName = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserName)!!
        val tvUserEmail = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserEmail)!!
        val tvUserPhone = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserPhone)!!
        val tbUserStatus = listItemView.findViewById<ToggleButton>(R.id.viewuseradapter_btnStatus)!!
        val ivUserAvatar = listItemView.findViewById<CircleImageView>(R.id.viewuseradapter_imageView)!!

        fun bindView(user: User) {
            tvUserName.text = user.name
            tvUserEmail.text = user.email
            tvUserPhone.text = user.phone

            //set avatar
            if (user.image != "")
                Picasso.get().load(user.image).resize(150, 150).into(ivUserAvatar)


            //set text for account status
            if (user.status == true){
                tbUserStatus.isChecked = true
            }
            else{
                tbUserStatus.isChecked = false
            }
            //change status on database using the status button
            tbUserStatus.setOnClickListener{
             //   Connect to firebase
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference("user")
            var statusChange: Boolean?

            if (user.status == true){
                statusChange = false
                //ban account
                val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                val ref = FirebaseDatabase.getInstance(url).getReference()
//                val query = ref.child("user").child(User.id)
//                query.removeValue()
//                Toast.makeText(it.context, "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show()
                //send mail to user
                val intent1 = Intent(Intent.ACTION_SENDTO)
                intent1.putExtra(Intent.EXTRA_SUBJECT, "Tài khoản 14VFilm của bạn đã bị khóa");
                intent1.putExtra(Intent.EXTRA_TEXT, "Tài khoản với tên người dùng "+ user.name + " của bạn đã bị khóa vì vi phạm chính sách người dùng");
                intent1.setData(Uri.parse("mailto:"+user.email));
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                it.context.startActivity(intent1)
            }
            else{
                statusChange = true
            }
            ref.child(user.id)
                .child("status")
                .setValue(statusChange)
            }

        }

    }



}