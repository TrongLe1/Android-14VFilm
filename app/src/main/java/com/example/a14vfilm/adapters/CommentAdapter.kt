package com.example.a14vfilm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Comment
import com.example.a14vfilm.models.UserLogin
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter (private val comment: List<Comment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val IVAvatar = listItemView.findViewById<CircleImageView>(R.id.IVDCAvatar)!!
        val TVName = listItemView.findViewById<TextView>(R.id.TVDCName)!!
        val RBRate = listItemView.findViewById<RatingBar>(R.id.RBDCRate)!!
        val TVComment = listItemView.findViewById<TextView>(R.id.TVCommentBox)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.adapter_comment, parent, false)
        return ViewHolder(contactView)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (comment[position].image != "")
            Picasso.get().load(comment[position].image).resize(150, 150).into(holder.IVAvatar)
        holder.TVName.text = comment[position].name
        holder.RBRate.rating = comment[position].rate
        holder.TVComment.text = comment[position].comment
    }

    override fun getItemCount(): Int {
        return comment.size
    }
}