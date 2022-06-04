package com.june0122.wakplus.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.june0122.wakplus.R
import com.june0122.wakplus.data.repository.impl.PreferencesRepositoryImpl
import com.june0122.wakplus.databinding.ActivityMainBinding
import com.june0122.wakplus.utils.listeners.DataLoadListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DataLoadListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var preferencesRepository: PreferencesRepositoryImpl

    override fun onStatusChanged(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.progressLoading.visibility = View.VISIBLE
            false -> binding.progressLoading.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            super.onCreate(savedInstanceState)
            setupTheme()
            binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
            val view = binding.root
            setContentView(view)

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            appBarConfiguration = AppBarConfiguration(
                setOf(R.id.home_dest, R.id.favorite_dest, R.id.settings_dest)
            )
            setupBottomNavMenu(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private suspend fun setupTheme() {
        val appTheme = preferencesRepository.flowThemes().first()
        this.setTheme(appTheme)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNavView
        bottomNav.setupWithNavController(navController)
    }

    fun setupActionBar(toolBar: Toolbar) {
        setSupportActionBar(toolBar)
        setupActionBarWithNavController(navController)
    }
}