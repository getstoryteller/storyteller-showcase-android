package com.getstoryteller.storytellersampleapp.features.main.bottomnav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.getstoryteller.storytellersampleapp.R
import com.getstoryteller.storytellersampleapp.features.main.PageState
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
  navController: NavHostController,
  onSetTopBarVisible: (Boolean) -> Unit,
  navigationState: PageState,
  onSetNavigationState: (PageState) -> Unit,
  onTriggerMomentReload: () -> Unit,
  onSetNavigationInterceptor: () -> NavigationInterceptor = { NavigationInterceptor.None },
) {
  if (navigationState == PageState.HOME) {
    BottomNavigation(
      modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
      backgroundColor = MaterialTheme.colors.background
    ) {
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val homeSelected = navBackStackEntry?.destination?.route == "home"
      val coroutineScope = rememberCoroutineScope()
      BottomNavigationItem(
        icon = {
          Icon(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = null,
            tint = if (homeSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
          )
        },
        label = {
          Text(
            text = "Home",
            color = if (homeSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
          )
        },
        selected = navBackStackEntry?.destination?.route == "home",
        onClick = {
          val interceptor = onSetNavigationInterceptor()
          if (interceptor is NavigationInterceptor.TargetRoute &&
            homeSelected && interceptor.shouldIntercept()
          ) {
            coroutineScope.launch {
              interceptor.onIntercepted()
            }
            return@BottomNavigationItem
          }
          onSetNavigationState(PageState.HOME)
          onSetTopBarVisible(true)
          navController.popUpTo("home")
        }
      )
      BottomNavigationItem(
        icon = {
          Icon(
            painter = painterResource(id = R.drawable.ic_watch),
            contentDescription = null,
            tint = if (!homeSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
          )
        },
        label = {
          Text(
            text = "Moments",
            color = if (!homeSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
          )
        },
        selected = navBackStackEntry?.destination?.route == "home/moments",
        onClick = {
          val interceptor = NavigationInterceptor.TargetRoute(
              targetRoute = "home/moments",
              shouldIntercept = { true },
              onIntercepted = {
                onTriggerMomentReload()
              },
            )

          if (!homeSelected && interceptor.shouldIntercept()) {
            coroutineScope.launch {
              interceptor.onIntercepted()
            }
            return@BottomNavigationItem
          }
          onSetNavigationState(PageState.HOME)
          onSetTopBarVisible(false)
          navController.popUpTo("home/moments")
        }
      )
    }
  }
}

fun NavController.popUpTo(destination: String) = navigate(destination) {
  popUpTo(graph.findStartDestination().id) {
    saveState = true
  }
  restoreState = true
}

