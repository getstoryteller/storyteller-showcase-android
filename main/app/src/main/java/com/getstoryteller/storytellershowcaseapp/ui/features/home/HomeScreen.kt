package com.getstoryteller.storytellershowcaseapp.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.ui.components.pullrefresh.rememberStorytellerPullToRefreshState
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.NavigationInterceptor
import com.getstoryteller.storytellershowcaseapp.ui.features.storyteller.StorytellerItem
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  sharedViewModel: MainViewModel,
  config: Config?,
  navController: NavController,
  isRefreshing: Boolean,
  modifier: Modifier = Modifier,
  onSetNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  onNavigateToLogin: () -> Unit = {},
) {
  LaunchedEffect(key1 = config?.configId ?: UUID.randomUUID().toString(), block = {
    config?.let {
      viewModel.loadHomePage(it)
    }
  })

  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()

  val pageUiState by viewModel.uiState.collectAsState()
  val loginState by sharedViewModel.loginUiState.collectAsState()

  val refreshState = rememberStorytellerPullToRefreshState()

  val listState = rememberLazyListState()
  val listItems = pageUiState.homeItems
  var columnHeightPx by remember {
    mutableIntStateOf(0)
  }
  LaunchedEffect(loginState.isLoggedIn, config) {
    if (!loginState.isLoggedIn && config == null) {
      onNavigateToLogin()
    }
  }
  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      config?.let {
        viewModel.loadHomePage(it)
      }
    }
  }
  Box(
    modifier =
    modifier
      .fillMaxSize()
      .nestedScroll(refreshState.nestedScrollConnection)
      .onGloballyPositioned {
        columnHeightPx = it.size.height
      },
  ) {
    if (!pageUiState.tabsEnabled) {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
      ) {
        itemsIndexed(items = listItems) { _, uiModel ->
          StorytellerItem(
            uiModel = uiModel,
            isRefreshing = pageUiState.isRefreshing || isRefreshing,
            navController = navController,
            roundTheme = config?.roundTheme,
            squareTheme = config?.squareTheme,
          ) {
            viewModel.hideStorytellerItem(uiModel.itemId)
          }
        }
      }
    } else {
      TabLayout(
        rootNavController = navController,
        sharedViewModel = sharedViewModel,
        parentState =
        TabLayoutUiState(
          tabs = pageUiState.tabs,
          isRefreshing = pageUiState.isRefreshing || isRefreshing,
          config = config,
        ),
        onSetNavigationInterceptor = onSetNavigationInterceptor,
      )
    }
  }
}
