package com.getstoryteller.storytellersampleapp.ui.utils

import androidx.navigation.NavController

fun NavController.isCurrentDestination(destination: String): Boolean {
  return currentDestination?.route == destination
}
