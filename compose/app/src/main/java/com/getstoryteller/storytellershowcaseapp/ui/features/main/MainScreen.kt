package com.getstoryteller.storytellershowcaseapp.ui.features.main

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.ui.features.account.AccountScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.account.AnalyticsOptionScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.account.OptionSelectScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.account.OptionSelectType
import com.getstoryteller.storytellershowcaseapp.ui.features.home.HomeScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.home.MoreScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.home.PageItemUiModel
import com.getstoryteller.storytellershowcaseapp.ui.features.link.LinkScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.login.LoginScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.accountOptionsScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.accountOptionsScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.accountScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.accountScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.analyticsScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.analyticsScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.homeScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.homeScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.linkScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.linkScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.momentsScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.momentsScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.moreScreenEnterTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.Transitions.moreScreenExitTransition
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.BottomNavigationBar
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.NavigationInterceptor
import com.getstoryteller.storytellershowcaseapp.ui.features.moments.MomentsScreen
import com.getstoryteller.storytellershowcaseapp.ui.utils.isCurrentDestination
import com.storyteller.Storyteller
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
  activity: Activity,
  navController: NavHostController,
  viewModel: MainViewModel,
  deepLinkData: String?,
  onLocationChanged: (String) -> Unit,
) {
  val mainPageUiState by viewModel.uiState.collectAsState()
  var navigationState by remember { mutableStateOf(PageState.HOME) }
  var title by remember { mutableStateOf("") }
  var momentsTabLoading by remember { mutableStateOf(false) }
  var homeTabLoading by remember { mutableStateOf(false) }
  val collection by remember(mainPageUiState.config) {
    mutableStateOf(mainPageUiState.config?.topLevelCollectionId ?: "")
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
      if (navigationState == PageState.LOGIN) return@Scaffold

      if (topBarVisible) {
        CenterAlignedTopAppBar(
          title = {
            if (navigationState != PageState.HOME) {
              Text(
                text = title,
                style =
                MaterialTheme.typography.titleLarge.copy(
                  fontSize = 20.sp,
                  fontWeight = FontWeight.W600,
                ),
                color = MaterialTheme.colorScheme.onBackground,
              )
            }
          },
          actions = {
            if (navigationState == PageState.HOME) {
              // The Storyteller SDK supports opening it's search experience using the
              // Storyteller.openSearch method. This can be triggered from wherever you
              // would like in your application. In this case, we show an example of doing
              // it from a main nav bar button
              var isSearchVisible by remember { mutableStateOf(false) }
              LaunchedEffect(key1 = Unit) {
                isSearchVisible = Storyteller.isSearchEnabled
              }

              if (isSearchVisible) {
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
            IconButton(
              onClick = {
                if (navigationState != PageState.HOME) {
                  navController.navigateUp()
                }
              },
            ) {
              if (navigationState != PageState.HOME) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        momentsTabLoading = momentsTabLoading,
        homeTabLoading = homeTabLoading,
        onTriggerMomentReload = {
          viewModel.triggerMomentsReloadData()
        },
      ) { navigationInterceptor }
    },
  ) { paddingValues ->
    // check home/moments on navController
    val bottomPadding = if (navController.isCurrentDestination("home/moments")) {
      paddingValues.calculateBottomPadding()
    } else {
      8.dp
    }

    Box(
      modifier =
      Modifier
        .fillMaxSize()
        .padding(bottom = bottomPadding),
    ) {
      NavHost(
        navController = navController,
        startDestination = "home",
      ) {
        val topPaddingEnabledModifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        composable(
          "home",
          enterTransition = homeScreenEnterTransition(),
          exitTransition = homeScreenExitTransition(),
        ) {
          navigationState = PageState.HOME
          topBarVisible = true
          HomeScreen(
            modifier = topPaddingEnabledModifier.navigationBarsPadding(),
            viewModel = hiltViewModel(key = mainPageUiState.config?.configId ?: "home"),
            sharedViewModel = viewModel,
            deepLinkData = deepLinkData,
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
            onLocationChanged = onLocationChanged,
            isHomeRefreshing = { homeTabLoading = it },
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
          route = "home/moments?clipId={clipId}",
          arguments = listOf(
            navArgument("clipId") {
              type = NavType.StringType
              nullable = true
              defaultValue = null
            },
          ),
          enterTransition = momentsScreenEnterTransition(),
          exitTransition = momentsScreenExitTransition(),
        ) { backStackEntry ->
          navigationState = PageState.HOME
          topBarVisible = false
          val clipId = remember(backStackEntry.arguments) {
            backStackEntry.arguments?.getString("clipId", null)
          }

          LaunchedEffect(Unit) {
            navigationInterceptor = NavigationInterceptor.None
          }
          MomentsScreen(
            collection = collection,
            startClip = clipId,
            sharedViewModel = viewModel,
            onSetTopBarVisible = {
              topBarVisible = it
            },
            onMomentsTabLoading = {
              momentsTabLoading = it
            },
            onLocationChanged = onLocationChanged,
          )
        }
        composable(
          "home/account",
          enterTransition = accountScreenEnterTransition(),
          exitTransition = accountScreenExitTransition(),
        ) {
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
          enterTransition = accountOptionsScreenEnterTransition(),
          exitTransition = accountOptionsScreenExitTransition(),
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
        composable(
          "account/analytics",
          enterTransition = analyticsScreenEnterTransition(),
          exitTransition = analyticsScreenExitTransition(),
        ) {
          navigationState = PageState.ACCOUNT
          title = "Analytics"
          AnalyticsOptionScreen(
            modifier = topPaddingEnabledModifier,
            accountViewModel = hiltViewModel(),
          )
        }
        composable(
          "moreClips/{model}",
          enterTransition = moreScreenEnterTransition(),
          exitTransition = moreScreenExitTransition(),
        ) { backStackEntry ->
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
              onLocationChanged = onLocationChanged,
            )
          }
        }
        composable(
          "moreStories/{model}",
          enterTransition = moreScreenEnterTransition(),
          exitTransition = moreScreenExitTransition(),
        ) { backStackEntry ->
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
              onLocationChanged = onLocationChanged,
            )
          }
        }
        composable(
          "home/link/{url}",
          arguments = listOf(navArgument("url") { type = NavType.StringType }),
          enterTransition = linkScreenEnterTransition(),
          exitTransition = linkScreenExitTransition(),
        ) { backStackEntry ->
          navigationState = PageState.LINK
          topBarVisible = true
          title = "Action Link"
          val url = backStackEntry.arguments?.getString("url")
          LinkScreen(link = url)
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
  LINK,
  LOGIN,
}
