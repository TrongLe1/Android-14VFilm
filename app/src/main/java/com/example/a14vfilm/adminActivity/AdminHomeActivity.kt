package com.example.a14vfilm.adminActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.a14vfilm.R
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.adminActivity.AdminManagementFragment
import com.example.a14vfilm.more.MoreFragment
import com.example.a14vfilm.sellerActivity.SellerManagementFragment
import com.example.a14vfilm.sellerActivity.SellerStatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminHomeActivity : AppCompatActivity() {
    private var bnvAdminHomeActivity: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        initComponent()
        initBottomNavigationView()

    }

    // set up component with ID
    private fun initComponent(){
        bnvAdminHomeActivity = findViewById(R.id.bnvAdminHomeActivity)

    }

    // set up Navigation Item of BottomNavigationView
    @SuppressLint("ResourceAsColor")
    private fun initBottomNavigationView(){
        setCurrentFragment(HomeFragment())
        supportActionBar!!.hide()
//        bnvSellerHomeActivity!!.setBackgroundColor(R.color.seller_menu)
        bnvAdminHomeActivity!!.setOnNavigationItemSelectedListener{
            // Get item ID of Menu item to set Fragment to FrameLayout
            when(it.itemId){
                R.id.menu_item_admin_home -> {
                    setCurrentFragment(HomeFragment())
                }

                R.id.menu_item_admin_management -> {
                    setCurrentFragment(AdminManagementFragment())
                }

                R.id.menu_item_admin_statistic -> {
                    setCurrentFragment(AdminStatisticsFragment())
                }

                R.id.menu_item_admin_more -> {
                    setCurrentFragment(MoreFragment())
                }

            }

            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flAdminHomeActivity,fragment)
            commit()
        }

}