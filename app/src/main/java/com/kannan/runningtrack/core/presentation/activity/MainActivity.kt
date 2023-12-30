package com.kannan.runningtrack.core.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.coreFragmentContainerView) as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setNavControllerWithBottomNavigation()

        setNavigationOnDestinationChangeListener()

    }

    private fun setNavControllerWithBottomNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setNavigationOnDestinationChangeListener() {
        navController.addOnDestinationChangedListener{_,destination,_ ->
           binding.bottomNavigationView.visibility =  when(destination.id){
                R.id.runFragment,R.id.statisticFragment,R.id.settingsFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }
}