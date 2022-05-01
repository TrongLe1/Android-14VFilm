package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.a14vfilm.R
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.more.MoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SellerHomeActivity : AppCompatActivity() {

    private var bnvSellerHomeActivity: BottomNavigationView? = null
//    private val sellerHomeActivityMenu: Menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_home)

        initComponent()
        initBottomNavigationView()

    }

    // set up component with ID
    private fun initComponent(){
        bnvSellerHomeActivity = findViewById(R.id.bnvSellerHomeActivity)

    }

    // set up Navigation Item of BottomNavigationView
    @SuppressLint("ResourceAsColor")
    private fun initBottomNavigationView(){
        setCurrentFragment(HomeFragment())
//        bnvSellerHomeActivity!!.setBackgroundColor(R.color.seller_menu)
        bnvSellerHomeActivity!!.setOnNavigationItemSelectedListener{

            // Get item ID of Menu item to set Fragment to FrameLayout
            when(it.itemId){

                R.id.menu_item_seller_home -> {
                    setCurrentFragment(HomeFragment())
//                    setCurrentFragment(SellerHomeFragment())
                    supportActionBar!!.title = "Trang chủ"
                }

                R.id.menu_item_seller_management -> {
                    setCurrentFragment(SellerManagementFragment())
                    supportActionBar!!.title = "Quản lý phim"
                }

                R.id.menu_item_seller_statistic -> {
                    setCurrentFragment(SellerStatisticFragment())
                    supportActionBar!!.title = "Thống kê phim"
                }

                R.id.menu_item_seller_more -> {
                    setCurrentFragment(MoreFragment())
//                    setCurrentFragment(SellerMoreFragment())
                    supportActionBar?.title = "Khác"
                }

            }

            true
        }
    }

    private fun setCurrentFragment(fragment:Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flSellerHomeActivity,fragment)
            commit()
        }


}

