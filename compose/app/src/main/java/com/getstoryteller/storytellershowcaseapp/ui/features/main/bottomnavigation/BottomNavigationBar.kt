package com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.ui.LocalStorytellerColorsPalette
import com.getstoryteller.storytellershowcaseapp.ui.features.main.PageState
import com.getstoryteller.storytellershowcaseapp.ui.utils.borderTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
  navController: NavHostController,
  momentsTabLoading: Boolean,
  homeTabLoading: Boolean,
  isMainScreenLoading: Boolean,
  onSetTopBarVisible: (Boolean) -> Unit,
  navigationState: PageState,
  onSetNavigationState: (PageState) -> Unit,
  onTriggerMomentReload: () -> Unit,
  onSetNavigationInterceptor: () -> NavigationInterceptor = { NavigationInterceptor.None },
) {
  if (navigationState != PageState.HOME) return
  NavigationBar(
    modifier = Modifier.borderTop(0.5.dp, LocalStorytellerColorsPalette.current.border),
    tonalElevation = 1.dp,
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    val debounceTime = 500L
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val homeSelected = navBackStackEntry?.destination?.route == "home"
    val coroutineScope = rememberCoroutineScope()
    var lastClickTime by remember { mutableStateOf(0L) }
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
      NavigationBarItem(
        colors =
        NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent,
        ),
        interactionSource = remember { MutableInteractionSource() },
        icon = {
          if (homeTabLoading && homeSelected) {
            CircularProgressIndicator(
              modifier = Modifier.size(24.dp),
              color = MaterialTheme.colorScheme.primary,
              strokeCap = StrokeCap.Round,
            )
          } else {
            Icon(
              painter = painterResource(id = R.drawable.ic_home),
              contentDescription = null,
              tint = if (homeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
          }
        },
        label = {
          Text(
            text = "Home",
            color = if (homeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
          )
        },
        selected = navBackStackEntry?.destination?.route == "home",
        onClick = {
          debounceClick(
            coroutineScope = coroutineScope,
            lastClickTime = lastClickTime,
            debounceTime = debounceTime,
            onUpdateLastClickTime = { lastClickTime = it },
          ) {
            val interceptor = onSetNavigationInterceptor()
            if (interceptor is NavigationInterceptor.TargetRoute &&
              homeSelected && interceptor.shouldIntercept()
            ) {
              coroutineScope.launch {
                interceptor.onIntercepted()
              }
              return@debounceClick
            }
            onSetNavigationState(PageState.HOME)
            onSetTopBarVisible(true)
            navController.popUpTo("home")
          }
        },
      )

      NavigationBarItem(
        colors =
        NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent,
        ),
        icon = {
          if (momentsTabLoading && !homeSelected) {
            CircularProgressIndicator(
              modifier = Modifier.size(24.dp),
              color = MaterialTheme.colorScheme.primary,
              strokeCap = StrokeCap.Round,
            )
          } else {
            Icon(
              painter = painterResource(id = R.drawable.ic_moments),
              contentDescription = null,
              tint = if (!homeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
          }
        },
        label = {
          Text(
            text = "Moments",
            color = if (!homeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
          )
        },
        enabled = !isMainScreenLoading,
        selected = navBackStackEntry?.destination?.route == "home/moments",
        onClick = {
          debounceClick(
            coroutineScope = coroutineScope,
            lastClickTime = lastClickTime,
            debounceTime = debounceTime,
            onUpdateLastClickTime = { lastClickTime = it },
          ) {
            val interceptor =
              NavigationInterceptor.TargetRoute(
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
              return@debounceClick
            }
            onSetNavigationState(PageState.HOME)
            onSetTopBarVisible(false)
            navController.popUpTo("home/moments")
          }
        },
      )
    }
  }
}

private object NoRippleTheme : RippleTheme {
  @Composable
  override fun defaultColor() = Color.Unspecified

  @Composable
  override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

fun NavController.popUpTo(
  destination: String,
) = navigate(destination) {
  popUpTo(graph.findStartDestination().id) {
    saveState = true
  }
  restoreState = true
}

inline fun debounceClick(
  coroutineScope: CoroutineScope,
  lastClickTime: Long,
  debounceTime: Long,
  crossinline onUpdateLastClickTime: (Long) -> Unit,
  crossinline action: () -> Unit,
) {
  val currentTime = System.currentTimeMillis()
  if (currentTime - lastClickTime > debounceTime) {
    onUpdateLastClickTime(currentTime)
    coroutineScope.launch {
      delay(debounceTime) // Ensure debounce delay
      action()
    }
  }
}
