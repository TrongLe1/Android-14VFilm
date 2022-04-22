package com.example.a14vfilm.sellerActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatisticViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment)
{
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> StatisticSummaryFragment()
            1 -> StatisticJoinerFragment()
            2 -> StatisticRevenueFragment()
            else -> {
                Fragment()
            }
        }
    }
}