package com.getstoryteller.storytellershowcaseapp.ui.features.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

  private var nullableBinding: FragmentMainBinding? = null
  private val binding get() = nullableBinding!!

  private lateinit var navController: NavController

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    nullableBinding = FragmentMainBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)
    val navHostFragment = childFragmentManager.findFragmentById(R.id.bottom_nav_host_container) as NavHostFragment
    navController = navHostFragment.navController
    setupBottomNavView()
  }

  private fun setupBottomNavView() {
    binding.bottomNavigationView.setupWithNavController(navController)
    binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
      NavigationUI.onNavDestinationSelected(menuItem, navController)
    }
  }
}
