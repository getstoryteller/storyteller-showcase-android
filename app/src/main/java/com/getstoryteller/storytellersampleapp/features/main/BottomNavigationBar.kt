package com.getstoryteller.storytellersampleapp.features.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.getstoryteller.storytellersampleapp.R

@Composable
fun BottomNavigationBar(
  navController: NavHostController,
  onSetTopBarVisible: (Boolean) -> Unit,
  navigationState: PageState,
  onSetNavigationState: (PageState) -> Unit,
  viewModel: MainViewModel
) {
  AnimatedVisibility(
    visible = navigationState == PageState.HOME,
    content = {
      BottomNavigation(
        backgroundColor = MaterialTheme.colors.background
      ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val homeSelected = navBackStackEntry?.destination?.route == "home"

        BottomNavigationItem(
          icon = {
            Icon(
              painter = painterResource(id = R.drawable.ic_home),
              contentDescription = null,
              tint = if (homeSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.onSurface
            )
          },
          label = {
            Text(
              text = "Home",
              color = if (homeSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.onSurface
            )
          },
          selected = navBackStackEntry?.destination?.route == "home",
          onClick = {
            navController.navigate("home") {
              onSetNavigationState(PageState.HOME)
              popUpTo(navController.graph.startDestinationId) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
            onSetTopBarVisible(true)
          }
        )
        BottomNavigationItem(
          icon = {
            Icon(
              painter = painterResource(id = R.drawable.ic_watch),
              contentDescription = null,
              tint = if (!homeSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.onSurface
            )
          },
          label = {
            Text(
              text = "Watch",
              color = if (!homeSelected) MaterialTheme.colors.onBackground else MaterialTheme.colors.onSurface
            )
          },
          selected = navBackStackEntry?.destination?.route == "home/moments",
          onClick = {
            if (navBackStackEntry?.destination?.route == "home/moments") {
              viewModel.triggerMomentsReloadData()
            }
            onSetNavigationState(PageState.HOME)
            onSetTopBarVisible(false)
            navController.navigate("home/moments") {
              popUpTo(navController.graph.startDestinationId) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          }
        )
      }
    })
}
