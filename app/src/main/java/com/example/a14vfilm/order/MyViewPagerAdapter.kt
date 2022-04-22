package com.example.a14vfilm.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment)
{
    override fun getItemCount(): Int {
        return 2//3
    }
    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return OrderedFragment()
            1 -> return ExpiredFragment()
            //2 -> return CancelledFragment()
            else -> {
                return Fragment()
            }
        }
    }
}