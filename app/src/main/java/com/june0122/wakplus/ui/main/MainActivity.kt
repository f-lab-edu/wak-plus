package com.june0122.wakplus.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.june0122.wakplus.R
import com.june0122.wakplus.databinding.ActivityMainBinding
import com.june0122.wakplus.utils.listeners.DataLoadListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DataLoadListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onStatusChanged(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.progressLoading.visibility = View.VISIBLE
            false -> binding.progressLoading.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNavView
        bottomNav.setupWithNavController(navController)
    }
}