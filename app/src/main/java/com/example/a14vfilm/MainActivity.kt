package com.example.a14vfilm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.library.LibraryFragment
import com.example.a14vfilm.more.MoreFragment
import com.example.a14vfilm.order.OrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FLMain,fragment)
            commit()
        }
}