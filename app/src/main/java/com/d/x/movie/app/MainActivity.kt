package com.d.x.movie.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.d.x.movie.app.fragments.FavoriteFragment
import com.d.x.movie.app.fragments.HomeFragment
import com.d.x.movie.app.fragments.SearchByDateFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val homeFragment = HomeFragment()
    val favoriteFragment = FavoriteFragment()
    val searchFragment = SearchByDateFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_button -> {
                    loadFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.favorite_button -> {
                    loadFragment(favoriteFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.calendar_button -> {
                    loadFragment(searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        loadFragment(homeFragment)
    }
}
