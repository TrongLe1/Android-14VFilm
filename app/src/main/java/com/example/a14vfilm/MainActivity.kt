package com.example.a14vfilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.library.LibraryFragment
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.example.a14vfilm.more.MoreFragment
import com.example.a14vfilm.order.OrderFragment
import com.example.a14vfilm.sellerActivity.FilmDetailActivity
import com.example.a14vfilm.sellerActivity.SellerFilmManagementActivity
import com.example.a14vfilm.sellerActivity.SellerUploadFilmActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    var bottomNavigationView: BottomNavigationView? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            UserLogin.info = User(
                currentUser!!.uid,
                currentUser.email!!,
                "",
                currentUser.displayName!!,
                "",
                "",
                currentUser.photoUrl.toString()
            )
        }

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
        supportActionBar!!.hide()

<<<<<<< Updated upstream
        change()

=======
>>>>>>> Stashed changes
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FLMain,fragment)
            commit()
        }

    private fun change(){
        val intent: Intent = Intent(this, SellerUploadFilmActivity::class.java)
        //val intent: Intent = Intent(this, FilmDetailActivity::class.java)

        startActivity(intent)
    }
}