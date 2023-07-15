package com.berkayozdag.sausocial.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.berkayozdag.sausocial.R.id
import com.berkayozdag.sausocial.common.util.setVisible
import com.berkayozdag.sausocial.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == id.postDetailFragment || destination.id == id.postCreateFragment) {
                binding.bottomNavigationView.setVisible(false)
            } else {
                binding.bottomNavigationView.setVisible(true)
            }
        }

        binding.bottomNavigationView.apply {
            NavigationUI.setupWithNavController(
                this,
                navController
            )
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                true
            }
            setOnItemReselectedListener { item ->
                navController.popBackStack(destinationId = item.itemId, inclusive = false)
            }
        }
    }

}