package com.example.a14vfilm.sellerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.a14vfilm.R

class FilmStatisticActivity : AppCompatActivity() {

    private var frameLayoutStatistic: FrameLayout? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_statistic)


        frameLayoutStatistic = findViewById(R.id.FLStatisticView)
        setFragment(OwnerFilmFragment())

    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FLMain,fragment)
            commit()
        }



}
