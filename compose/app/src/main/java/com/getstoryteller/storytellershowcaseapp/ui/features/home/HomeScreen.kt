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
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.storyteller.ui.compose.components.lists.grid.rememberStorytellerGridState
import com.storyteller.ui.compose.components.lists.row.StorytellerDataState
import com.storyteller.ui.compose.components.lists.row.rememberStorytellerRowState
import kotlinx.coroutines.launch
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
  onLocationChanged: (String) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()
  val pageUiState by viewModel.uiState.collectAsState()
  val loginState by sharedViewModel.loginUiState.collectAsState()
  val innerListStates = remember { mutableStateMapOf<String, StorytellerDataState>() }
  val refreshState = rememberStorytellerPullToRefreshState()

  val listState = rememberLazyListState()
  val listItems = pageUiState.homeItems
  var columnHeightPx by remember {
    mutableIntStateOf(0)
  }
  if (refreshState.isRefreshing) {
    LaunchedEffect(true) {
      scope.launch {
        sharedViewModel.refreshMainPage()
      }
    }
  }

  fun reloadData(
    config: Config?,
  ) {
    config?.also(viewModel::loadHomePage)
    innerListStates.values.forEach { listState ->
      scope.launch { listState.reloadData() }
    }
  }

  LaunchedEffect(
    key1 = config?.configId ?: UUID.randomUUID().toString(),
    block = {
      config?.let {
        reloadData(it)
      }
      refreshState.endRefresh()
    },
  )
  LaunchedEffect(loginState.isLoggedIn, config) {
    if (!loginState.isLoggedIn && config == null) {
      onNavigateToLogin()
    }
  }
  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      config?.let {
        reloadData(it)
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
      LaunchedEffect(key1 = Unit) {
        onLocationChanged("Home")
      }
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
      ) {
        itemsIndexed(items = listItems) { _, uiModel ->
          val innerListState = innerListStates.getOrPut(uiModel.itemId) {
            if (uiModel.layout == com.getstoryteller.storytellershowcaseapp.remote.entities.LayoutType.ROW) {
              rememberStorytellerRowState()
            } else {
              rememberStorytellerGridState()
            }
          }
          StorytellerItem(
            uiModel = uiModel,
            navController = navController,
            roundTheme = config?.roundTheme,
            squareTheme = config?.squareTheme,
            state = innerListState,
          ) {
            viewModel.hideStorytellerItem(uiModel.itemId)
          }
        }
      }
      PullToRefreshContainer(
        modifier = Modifier.align(Alignment.TopCenter),
        state = refreshState,
      )
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
        onLocationChanged = onLocationChanged,
      )
    }
  }
}
