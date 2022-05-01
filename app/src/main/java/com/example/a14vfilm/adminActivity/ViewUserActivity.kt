package com.example.a14vfilm.adminActivity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.ViewUserAdapter
import com.example.a14vfilm.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ViewUserActivity: AppCompatActivity() {

    private var rcvViewUser: RecyclerView? = null
    private var userSearch: androidx.appcompat.widget.SearchView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_users)

        // Initialize the required components
        initComponent()

        //create a list of user for adapter
        val userList = ArrayList<User>()
        //url for firebase database (change later)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("user")
        val adapterViewUser = ViewUserAdapter(userList)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (singleSnapshot in snapshot.children) {
                    val id = singleSnapshot.child("id").getValue<String>()
                    val email = singleSnapshot.child("email").getValue<String>()
                    val password = singleSnapshot.child("password").getValue<String>()
                    val name = singleSnapshot.child("name").getValue<String>()
                    val address = singleSnapshot.child("address").getValue<String>()
                    val phone = singleSnapshot.child("phone").getValue<String>()
                    val image = singleSnapshot.child("image").getValue<String>()
                    val status = singleSnapshot.child("status").getValue<Boolean>()
                    //userList.add(0, User(id!!, email!! , password!!, name!!, address!!, phone!!, status!!))
//                    userList.add(0, User(id!!, email!! , password!!, name!!, address!!, phone!!,image!!, status!!))
                }

                rcvViewUser!!.adapter = adapterViewUser
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        rcvViewUser!!.adapter = adapterViewUser

        findViewById<FloatingActionButton>(R.id.viewusers_fltbtnAddNewUser).setOnClickListener{
            val intent = Intent(this, AddNewUserAdminActivity::class.java)
            startActivity(intent)
        }

        userSearch!!.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterViewUser.filter.filter(newText)
                return false
            }

        })

    }

    private fun initComponent(){
        rcvViewUser = findViewById(R.id.viewusers_rcvUsersManagement)
        userSearch = findViewById(R.id.viewusers_svUserSearch)

        rcvViewUser!!.layoutManager = LinearLayoutManager(this)

    }

}