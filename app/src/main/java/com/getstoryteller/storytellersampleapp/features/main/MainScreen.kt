package com.getstoryteller.storytellersampleapp.features.main

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.getstoryteller.storytellersampleapp.R
import com.getstoryteller.storytellersampleapp.features.account.AccountScreen
import com.getstoryteller.storytellersampleapp.features.account.OptionSelectScreen
import com.getstoryteller.storytellersampleapp.features.account.OptionSelectType
import com.getstoryteller.storytellersampleapp.features.home.HomeScreen
import com.getstoryteller.storytellersampleapp.features.home.MoreScreen
import com.getstoryteller.storytellersampleapp.features.home.PageItemUiModel
import com.getstoryteller.storytellersampleapp.features.login.LoginDialog
import com.getstoryteller.storytellersampleapp.features.main.bottomnav.BottomNavigationBar
import com.getstoryteller.storytellersampleapp.features.main.bottomnav.NavigationInterceptor
import com.getstoryteller.storytellersampleapp.features.watch.MomentsScreen
import com.skydoves.cloudy.Cloudy
import com.storyteller.Storyteller
import com.storyteller.ui.pager.StorytellerClipsFragment
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
  activity: Activity, navController: NavHostController, viewModel: MainViewModel, onCommit: (
    fragment: Fragment,
    tag: String,
  ) -> (FragmentTransaction.(containerId: Int) -> Unit), getClipsFragment: () -> StorytellerClipsFragment?
) {
  val isLoginDialogVisible = viewModel.loginUiState.collectAsState()
  val mainPageUiState by viewModel.uiState.collectAsState()
  var navigationState by remember {
    mutableStateOf(PageState.HOME)
  }
  var title by remember {
    mutableStateOf("")
  }

  var topBarVisible by remember {
    mutableStateOf(true)
  }

  var navigationInterceptor by remember {
    mutableStateOf<NavigationInterceptor>(
      NavigationInterceptor.None,
    )
  }

  Scaffold(
    topBar = {
      if (topBarVisible) {
        TopAppBar(backgroundColor = MaterialTheme.colors.background,
          elevation = 0.dp,
          contentColor = MaterialTheme.colors.onBackground,
          title = {
            if (navigationState != PageState.HOME) {
              Text(text = title)
            }
          },
          actions = {
            if (navigationState == PageState.HOME) {
              IconButton(
                onClick = {
                  Storyteller.openSearch(activity)
                }, enabled = !mainPageUiState.isMainScreenLoading
              ) {
                Icon(
                  imageVector = Icons.Filled.Search, contentDescription = "", tint = MaterialTheme.colors.onBackground
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
                }, enabled = !mainPageUiState.isMainScreenLoading
              ) {
                Icon(
                  imageVector = Icons.Filled.AccountCircle,
                  contentDescription = "",
                  tint = MaterialTheme.colors.onBackground
                )
              }
            }
          },
          navigationIcon = {
            IconButton(onClick = {
              navController.navigateUp()
            }) {
              if (navigationState != PageState.HOME) Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground
              )
              else {
                Icon(
                  painter = painterResource(id = R.drawable.ic_logo_icon),
                  contentDescription = null,
                  tint = Color.Unspecified
                )
              }

            }
          })
      }
    }, bottomBar = {
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
    }) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
    ) {
      NavHost(
        navController = navController, startDestination = "home"
      ) {
        composable("home") {
          navigationState = PageState.HOME
          HomeScreen(
            viewModel = hiltViewModel(key = mainPageUiState.config?.configId ?: "home"),
            sharedViewModel = viewModel,
            config = mainPageUiState.config,
            navController = navController,
            isRefreshing = mainPageUiState.isHomeRefreshing,
            onSetNavigationInterceptor = {
              navigationInterceptor = it
            }
          )
        }
        composable("home/moments") {
          navigationState = PageState.HOME
          MomentsScreen(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            config = mainPageUiState.config,
            tag = "watch",
            onCommit = onCommit,
            getClipsFragment = getClipsFragment,
            sharedViewModel = viewModel
          )
        }
        composable("home/account") {
          navigationState = PageState.ACCOUNT
          title = "Account"
          AccountScreen(
            navController = navController,
            viewModel = hiltViewModel(),
            sharedViewModel = viewModel,
            config = mainPageUiState.config,
            onLogout = {
              navigationState = PageState.HOME
              viewModel.logout()
              navController.navigate("home")
            })
        }
        composable("account/{option}") {
          navigationState = PageState.ACCOUNT
          val option = OptionSelectType.valueOf(it.arguments?.getString("option")!!)
          title = option.title
          OptionSelectScreen(
            navController = navController,
            viewModel = hiltViewModel(),
            optionSelectType = option,
            config = mainPageUiState.config!!
          )
        }
        composable("moreClips/{model}") { backStackEntry ->
          val uiModel: PageItemUiModel? = Json.decodeFromString(backStackEntry.arguments?.getString("model")!!)
          uiModel?.let {
            navigationState = PageState.MORE
            title = it.title
            MoreScreen(
              pageItemUiModel = it,
              viewModel = hiltViewModel(),
              navController = navController,
              config = mainPageUiState.config
            )
          }
        }
        composable("moreStories/{model}") { backStackEntry ->
          val uiModel: PageItemUiModel? = Json.decodeFromString(backStackEntry.arguments?.getString("model")!!)
          uiModel?.let {
            navigationState = PageState.MORE
            title = it.title
            MoreScreen(
              pageItemUiModel = it,
              viewModel = hiltViewModel(),
              navController = navController,
              config = mainPageUiState.config
            )
          }
        }
      }
      if (mainPageUiState.isMainScreenLoading) {
        CircularProgressIndicator(
          modifier = Modifier
            .padding(16.dp)
            .background(color = MaterialTheme.colors.surface)
            .align(Alignment.Center)
        )
      }
    }
  }
  if (isLoginDialogVisible.value.isLoggedIn.not()) {
    Cloudy(radius = 25) {
      Box(
        modifier = Modifier
          .fillMaxSize()
      )
    }
    LoginDialog(viewModel)
  }
}

enum class PageState {
  HOME, ACCOUNT, MORE
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
  Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
  else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
