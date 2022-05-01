package com.example.a14vfilm.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Rating
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.example.a14vfilm.models.User
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.ViewFilmDetailActivity
import com.example.a14vfilm.detail.DetailActivity
import com.example.a14vfilm.models.Film
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.coroutines.coroutineContext

class ViewFilmsAdapter (private val filmList: List<Film>): RecyclerView.Adapter<ViewFilmsAdapter.ViewHolder>(),
    Filterable {

    var onItemClick: ((Film) -> Unit)? = null
    var filterListResult: List<Film> = filmList

    override fun onBindViewHolder(holder: ViewFilmsAdapter.ViewHolder, position: Int) {
        val film = filterListResult[position]

        holder.bindView(film)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(film)
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("Film", film)
            it.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewFilmsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_view_film_admin,parent,false))
    }

    override fun getItemCount(): Int {
        return filterListResult.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterListResult = filmList
                } else {
                    val resultList = ArrayList<Film>()
                    for (row in filmList) {
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
                filterListResult = results?.values as ArrayList<Film>
                notifyDataSetChanged()
            }

        }
    }

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val tvFilmName = listItemView.findViewById<TextView>(R.id.viewfilmadmin_tvFilmName)
        val rbFilmRate = listItemView.findViewById<RatingBar>(R.id.viewfilmadmin_rbFilmRate)
        val tvFilmDescription = listItemView.findViewById<TextView>(R.id.viewfilmadmin_tvFilmDescription)
        val tbFilmStatus = listItemView.findViewById<ToggleButton>(R.id.viewfilmadmin_btnStatus)
        val ivFilmImage = listItemView.findViewById<ImageView>(R.id.viewfilmadmin_imageView)

        fun bindView(film: Film) {
            tvFilmName.text = film.name
            rbFilmRate.rating = film.rate
            tvFilmDescription.text = film.description

            //set image for film
            if (film.image != "")
                Picasso.get().load(film.image).resize(150, 150).into(ivFilmImage)

            //set text for film status
            if (film.status == true){
                tbFilmStatus.isChecked = true
            }
            else{
                tbFilmStatus.isChecked = false
            }

            //change status on database using the status button
            tbFilmStatus.setOnClickListener{
                //   Connect to firebase
                val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                val ref = FirebaseDatabase.getInstance(url).getReference()
                var statusChange: Boolean?

                if (film.status == true){
                    statusChange = false

                    //find user information by query firebase
                    val query = ref.child("user").child(film.seller)
                    query.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                                val userEmail = snapshot.child("email").getValue<String>()
                                val userName = snapshot.child("name").getValue<String>()

                                //send email
                                BackgroundMail.newBuilder(it.context)
                                    .withUsername("14vfilmquantrivien@gmail.com")
                                    .withPassword("14vfilmquantrivien123")
                                    .withMailto(userEmail.toString())
                                    .withType(BackgroundMail.TYPE_PLAIN)
                                    .withSubject("Phim trên 14VFilm của bạn đã bị khóa")
                                    .withBody("Bộ phim " +film.name+ " của tài khoản "+ userName.toString() + " của bạn đã bị khóa vì vi phạm chính sách kiểm duyệt")
                                    .withOnSuccessCallback(BackgroundMail.OnSuccessCallback {
                                        //do some magic
                                    })
                                    .withOnFailCallback(BackgroundMail.OnFailCallback {
                                        //do some magic
                                    })
                                    .send()

                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })


                }
                else{
                    statusChange = true
                }
                ref.child("film")
                    .child(film.id)
                    .child("status")
                    .setValue(statusChange)
                //notifyDataSetChanged()
            }
        }

    }



}