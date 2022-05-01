package com.example.a14vfilm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.library.LibraryFragment
import com.example.a14vfilm.login.LoginActivity
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.example.a14vfilm.more.InfoActivity
import com.example.a14vfilm.more.MoreFragment
import com.example.a14vfilm.order.OrderFragment
import com.example.a14vfilm.sellerActivity.SellerHomeActivity
import com.example.a14vfilm.sellerActivity.SellerUploadFilmActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson





class MainActivity : AppCompatActivity() {
    var bottomNavigationView: BottomNavigationView? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        val sharedPreference = getSharedPreferences("UserLogin", MODE_PRIVATE)
//        val json = sharedPreference.getString("user", "")
//        val gson = Gson()
//        val user = gson.fromJson(json, User::class.java)
//
//        mAuth = FirebaseAuth.getInstance()
//        val currentUser = mAuth!!.currentUser
//        if (currentUser != null) {
//            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
//            val ref = FirebaseDatabase.getInstance(url).getReference("user")
//            val query = ref.orderByChild("id").equalTo(currentUser!!.uid)
//            query.addValueEventListener(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (singleSnapshot in snapshot.children) {
//                        val id = singleSnapshot.child("id").getValue<String>()
//                        val email = singleSnapshot.child("email").getValue<String>()
//                        val name = singleSnapshot.child("name").getValue<String>()
//                        val address = singleSnapshot.child("address").getValue<String>()
//                        val phone = singleSnapshot.child("phone").getValue<String>()
//                        val image = singleSnapshot.child("image").getValue<String>()
//                        val status = singleSnapshot.child("status").getValue<Boolean>()
//                        val role = singleSnapshot.child("role").getValue<Int>()
//                        UserLogin.info = User(
//                            id!!,
//                            email!!,
//                            "",
//                            name!!,
//                            address!!,
//                            phone!!,
//                            image!!,
//                            status!!,
//                            role!!
//                        )
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {}
//            })
//            userUI()
//        }
//        else if (json != "") {
//            UserLogin.info = user
//            if (UserLogin.info!!.role == 0)
//                userUI()
//            else if (UserLogin.info!!.role == 1) {
//                finish()
//                sellerUI()
//            }
//        }
//        else {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivityForResult(intent, 102)
//        }
//        supportActionBar!!.hide()
        sellerUI()

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FLMain,fragment)
            commit()
        }

    private fun sellerUI(){
        val intent: Intent = Intent(this, SellerHomeActivity::class.java)
        //val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (UserLogin.info!!.role == 0)
            userUI()
        else if (UserLogin.info!!.role == 1) {
            finish()
            sellerUI()
        }
    }

    private fun userUI(){
        val homeFragment = HomeFragment()
        val libraryFragment = LibraryFragment()
        val orderFragment = OrderFragment()
        val moreFragment = MoreFragment()
        setCurrentFragment(homeFragment)
        bottomNavigationView = findViewById(R.id.BNVMain)
        bottomNavigationView!!.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home->setCurrentFragment(homeFragment)
                R.id.library->setCurrentFragment(libraryFragment)
                R.id.order->setCurrentFragment(orderFragment)
                R.id.more->setCurrentFragment(moreFragment)
            }
            true
        }
    }
}