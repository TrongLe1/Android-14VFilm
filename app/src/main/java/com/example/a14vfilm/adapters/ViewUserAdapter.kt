package com.example.a14vfilm.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail.OnFailCallback
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail.OnSuccessCallback
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.ViewUserDetailActivity
import com.example.a14vfilm.models.User
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class ViewUserAdapter (private val userList: List<User>): RecyclerView.Adapter<ViewUserAdapter.ViewHolder>(), Filterable {

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
            intent.putExtra("userRole", user.role)
            it.context.startActivity(intent)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewUserAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_view_user, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvUserName = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserName)!!
        val tvUserEmail = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserEmail)!!
        val tvUserRole = listItemView.findViewById<TextView>(R.id.viewuseradapter_tvUserRole)!!
        val tbUserStatus = listItemView.findViewById<ToggleButton>(R.id.viewuseradapter_btnStatus)!!
        val ivUserAvatar = listItemView.findViewById<CircleImageView>(R.id.viewuseradapter_imageView)!!

        fun bindView(user: User){
            tvUserName.text = user.name
            tvUserEmail.text = user.email
            var userRole:String? = null
            if (user.role == 0){
                userRole = "Người mua"
            }
            else if (user.role == 1){
                userRole = "Người bán"
            }
            else if (user.role == 2){
                userRole = "Quản trị viên"
            }
            tvUserRole.text = userRole

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
                    user.status = false
                    tbUserStatus.isChecked = false

                    //Automail
                    BackgroundMail.newBuilder(it.context)
                        .withUsername("14vfilmquantrivien@gmail.com")
                        .withPassword("14vfilmquantrivien123")
                        .withMailto(user.email)
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Tài khoản 14VFilm của bạn đã bị khóa")
                        .withBody("Tài khoản với tên người dùng "+ user.name + " của bạn đã bị khóa vì vi phạm chính sách người dùng")
                        .withOnSuccessCallback(OnSuccessCallback {
                            //do some magic
                        })
                        .withOnFailCallback(OnFailCallback {
                            //do some magic
                        })
                        .send()

//                    val intent1 = Intent(Intent.ACTION_SENDTO)
//                    intent1.putExtra(Intent.EXTRA_SUBJECT, "Tài khoản 14VFilm của bạn đã bị khóa");
//                    intent1.putExtra(Intent.EXTRA_TEXT, "Tài khoản với tên người dùng "+ user.name + " của bạn đã bị khóa vì vi phạm chính sách người dùng");
//                    intent1.setData(Uri.parse("mailto:"+user.email));
//                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//                    it.context.startActivity(intent1)
                }
                else{
                    tbUserStatus.isChecked = true
                    user.status = true
                    statusChange = true
                }
                ref.child(user.id)
                    .child("status")
                    .setValue(statusChange)
                //notifyDataSetChanged()
            }
        }

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




}