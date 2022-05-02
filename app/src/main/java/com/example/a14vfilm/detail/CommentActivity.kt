package com.example.a14vfilm.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.CommentAdapter
import com.example.a14vfilm.models.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration

class CommentActivity : AppCompatActivity() {
    var RVComment: RecyclerView? = null
    var TVComment: TextView? = null
    var IBBack: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        RVComment = findViewById(R.id.RVComment)
        TVComment = findViewById(R.id.TVComment)
        IBBack = findViewById(R.id.IBComBack)

        val id = intent.getStringExtra("ID")
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RVComment!!.layoutManager = layoutManager
        RVComment!!.addItemDecoration(LayoutMarginDecoration(1, 30))
        val commentList = ArrayList<Comment>()
        val adapter = CommentAdapter(commentList)

        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("transaction")
        val query = ref.orderByChild("film").equalTo(id)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val comment = singleSnapshot.child("comment").getValue<String>()
                    val rate = singleSnapshot.child("rate").getValue<Float>()
                    val user = singleSnapshot.child("user").getValue<String>()
                    val sRef = FirebaseDatabase.getInstance(url).getReference("user")
                    val sQuery = sRef.orderByChild("id").equalTo(user)
                    sQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (singleSnapshot in snapshot.children) {
                                val name = singleSnapshot.child("name").getValue<String>()
                                val image = singleSnapshot.child("image").getValue<String>()
                                commentList.add(Comment(image!!, name!!, rate!!, comment!!))
                            }
                            RVComment!!.adapter = adapter
                        }
                        override fun onCancelled(error: DatabaseError) {}

                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        IBBack!!.setOnClickListener {
            finish()
        }
        supportActionBar!!.hide()
    }
}