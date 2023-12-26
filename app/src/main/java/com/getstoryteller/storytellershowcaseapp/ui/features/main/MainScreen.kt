package com.getstoryteller.storytellershowcaseapp.ui.features.main

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.ui.features.account.AccountScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.account.OptionSelectScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.account.OptionSelectType
import com.getstoryteller.storytellershowcaseapp.ui.features.home.HomeScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.home.MoreScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.home.PageItemUiModel
import com.getstoryteller.storytellershowcaseapp.ui.features.login.LoginScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.BottomNavigationBar
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.NavigationInterceptor
import com.getstoryteller.storytellershowcaseapp.ui.features.moments.MomentsScreen
import com.getstoryteller.storytellershowcaseapp.ui.utils.isCurrentDestination
import com.storyteller.Storyteller
import com.storyteller.ui.pager.StorytellerClipsFragment
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
  activity: Activity,
  navController: NavHostController,
  viewModel: MainViewModel,
  onCommit: (
    fragment: Fragment,
    tag: String,
  ) -> (FragmentTransaction.(containerId: Int) -> Unit),
  getClipsFragment: () -> StorytellerClipsFragment?,
) {
  val mainPageUiState by viewModel.uiState.collectAsState()
  var navigationState by remember { mutableStateOf(PageState.HOME) }
  var title by remember { mutableStateOf("") }

  var topBarVisible by remember {
    mutableStateOf(true)
  }

  var navigationInterceptor by remember {
    mutableStateOf<NavigationInterceptor>(
      NavigationInterceptor.None,
    )
  }

//  LaunchedEffect(navController.currentBackStackEntryAsState().value) {
//    when (navController.currentDestination?.route) {
//      "home/moments" -> {
//        setStatusBarColor(activity, Color.Transparent, false)
//      }
//
//      else -> setStatusBarColor(activity, topBarColor, !isSystemDark)
//    }
//  }

  Scaffold(
    topBar = {
      if (navigationState == PageState.LOGIN) return@Scaffold

      if (topBarVisible) {
        TopAppBar(
          title = {
            if (navigationState != PageState.HOME) {
              Text(text = title)
            }
          },
          actions = {
            if (navigationState == PageState.HOME) {
              // The Storyteller SDK supports opening it's search experience using the
              // Storyteller.openSearch method. This can be triggered from wherever you
              // would like in your application. In this case, we show an example of doing
              // it from a main nav bar button

              IconButton(
                onClick = {
                  Storyteller.openSearch(activity)
                },
                enabled = !mainPageUiState.isMainScreenLoading,
              ) {
                Icon(
                  imageVector = Icons.Filled.Search,
                  contentDescription = "Open Search",
                  tint = MaterialTheme.colorScheme.onBackground,
                )
              }
              IconButton(
                onClick = {
                  navigationState = PageState.ACCOUNT
                  navController.navigate("home/account") {
                    popUpTo(navController.graph.startDestinationId) {
                      saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                  }
                },
                enabled = !mainPageUiState.isMainScreenLoading,
              ) {
                Icon(
                  imageVector = Icons.Filled.AccountCircle,
                  contentDescription = "Open Account",
                  tint = MaterialTheme.colorScheme.onBackground,
                )
              }
            }
          },
          navigationIcon = {
            IconButton(onClick = {
              navController.navigateUp()
            }) {
              if (navigationState != PageState.HOME) {
                Icon(
                  imageVector = Icons.Filled.ArrowBack,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onBackground,
                )
              } else {
                Icon(
                  painter = painterResource(id = R.drawable.ic_logo_icon),
                  contentDescription = null,
                  tint = Color.Unspecified,
                )
              }
            }
          },
        )
      }
    },
    bottomBar = {
      if (navigationState == PageState.LOGIN) return@Scaffold

      BottomNavigationBar(
        navController = navController,
        onSetTopBarVisible = {
          topBarVisible = it
        },
        navigationState = navigationState,
        onSetNavigationState = {
          navigationState = it
        },
        onTriggerMomentReload = {
          viewModel.triggerMomentsReloadData()
        },
        onSetNavigationInterceptor = { navigationInterceptor },
      )
    },
  ) { paddingValues ->
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(bottom = paddingValues.calculateBottomPadding()),
    ) {
      NavHost(
        navController = navController,
        startDestination = "home",
      ) {
        val topPaddingEnabledModifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        composable("home", enterTransition = {
          if (initialState.destination.route == "home/moments") {
            EnterTransition.None
          } else {
            slideIntoContainer(
              towards = Right,
              animationSpec = tween(700),
            )
          }
        }, exitTransition = {
          if (targetState.destination.route == "home/moments") {
            ExitTransition.None
          } else {
            slideOutOfContainer(
              towards = Left,
              animationSpec = tween(700),
            )
          }
        }) {
          navigationState = PageState.HOME
          LaunchedEffect(Unit) {
            topBarVisible = true
          }
          HomeScreen(
            modifier = topPaddingEnabledModifier,
            viewModel = hiltViewModel(key = mainPageUiState.config?.configId ?: "home"),
            sharedViewModel = viewModel,
            config = mainPageUiState.config,
            navController = navController,
            isRefreshing = mainPageUiState.isHomeRefreshing,
            onSetNavigationInterceptor = {
              navigationInterceptor = it
            },
            onNavigateToLogin = {
              navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                  inclusive = true
                }
              }
            },
          )
        }
        composable("login") {
          navigationState = PageState.LOGIN
          LoginScreen(modifier = topPaddingEnabledModifier, viewModel = viewModel) {
            navController.navigate("home") {
              popUpTo("home") {
                inclusive = true
              }
            }
          }
        }
        composable(
          "home/moments",
          enterTransition = { EnterTransition.None },
          exitTransition = { ExitTransition.None },
        ) {
          navigationState = PageState.HOME
          LaunchedEffect(Unit) {
            navigationInterceptor = NavigationInterceptor.None
          }
          MomentsScreen(
            modifier = Modifier,
            config = mainPageUiState.config,
            tag = "moments",
            onCommit = onCommit,
            getClipsFragment = getClipsFragment,
            sharedViewModel = viewModel,
          )
        }
        composable("home/account", enterTransition = {
          if (initialState.destination.route?.startsWith("account") == true) {
            slideIntoContainer(
              towards = Right,
              animationSpec = tween(700),
            )
          } else {
            slideIntoContainer(
              towards = Left,
              animationSpec = tween(700),
            )
          }
        }, exitTransition = {
          if (targetState.destination.route?.startsWith("account") == true) {
            slideOutOfContainer(
              towards = Left,
              animationSpec = tween(700),
            )
          } else {
            slideOutOfContainer(
              towards = Right,
              animationSpec = tween(700),
            )
          }
        }) {
          navigationState = PageState.ACCOUNT
          title = "Account"
          AccountScreen(
            modifier = topPaddingEnabledModifier,
            navController = navController,
            viewModel = hiltViewModel(),
            sharedViewModel = viewModel,
            config = mainPageUiState.config,
            onLogout = {
              viewModel.logout()
            },
          )
        }
        composable(
          "account/{option}",
          enterTransition = { slideIntoContainer(towards = Left, animationSpec = tween(700)) },
          exitTransition = { slideOutOfContainer(towards = Right, animationSpec = tween(700)) },
        ) {
          navigationState = PageState.ACCOUNT
          val option = OptionSelectType.valueOf(it.arguments?.getString("option")!!)
          title = option.title
          OptionSelectScreen(
            modifier = topPaddingEnabledModifier,
            navController = navController,
            viewModel = hiltViewModel(),
            sharedViewModel = viewModel,
            optionSelectType = option,
            config = mainPageUiState.config!!,
          )
        }
        composable("moreClips/{model}", enterTransition = {
          slideIntoContainer(
            towards = Left,
            animationSpec = tween(700),
          )
        }, exitTransition = {
          slideOutOfContainer(
            towards = Right,
            animationSpec = tween(700),
          )
        }) { backStackEntry ->
          val uiModel: PageItemUiModel? =
            Json.decodeFromString(
              backStackEntry.arguments?.getString("model")!!,
            )
          uiModel?.let {
            navigationState = PageState.MORE
            title = it.title
            MoreScreen(
              modifier = topPaddingEnabledModifier,
              pageItemUiModel = it,
              navController = navController,
              config = mainPageUiState.config,
            )
          }
        }
        composable("moreStories/{model}", enterTransition = {
          slideIntoContainer(
            towards = Left,
            animationSpec = tween(700),
          )
        }, exitTransition = {
          slideOutOfContainer(
            towards = Right,
            animationSpec = tween(700),
          )
        }) { backStackEntry ->
          val uiModel: PageItemUiModel? =
            Json.decodeFromString(
              backStackEntry.arguments?.getString("model")!!,
            )
          uiModel?.let {
            navigationState = PageState.MORE
            title = it.title
            MoreScreen(
              modifier = topPaddingEnabledModifier,
              pageItemUiModel = it,
              navController = navController,
              config = mainPageUiState.config,
            )
          }
        }
      }
      if (mainPageUiState.isMainScreenLoading && navController.isCurrentDestination("home")) {
        CircularProgressIndicator(
          modifier =
            Modifier
              .padding(16.dp)
              .background(color = Color.Transparent)
              .align(Alignment.Center),
        )
      }
    }
  }
}

enum class PageState {
  HOME,
  ACCOUNT,
  MORE,
  LOGIN,
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
  when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else ->
      @Suppress("DEPRECATION")
      getParcelable(key)
        as? T
  }

@Suppress("DEPRECATION")
fun setStatusBarColor(
  activity: Activity,
  color: Color,
  useDarkIcons: Boolean,
) {
  activity.window.statusBarColor = color.toArgb()

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    activity.window.insetsController?.setSystemBarsAppearance(
      if (useDarkIcons) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
      WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
    )
  } else {
    activity.window.decorView.systemUiVisibility =
      if (useDarkIcons) {
        activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      } else {
        activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
      }
  }
}
