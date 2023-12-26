package com.getstoryteller.storytellershowcaseapp.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(
  tabId: String,
  viewModel: TabViewModel,
  sharedViewModel: MainViewModel,
  rootNavController: NavController,
  isRefreshing: Boolean,
  config: Config?,
  onSetTopNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  onShouldInterceptTopNavigation: () -> Boolean = { false },
  onSetBottomNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  setParentBottomNavigationInterceptor: () -> Unit = {},
) {
  LaunchedEffect(key1 = tabId, block = {
    viewModel.loadTab(tabId)
  })

  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()

  val pageUiState by viewModel.uiState.collectAsState()

  val refreshState = rememberStorytellerPullToRefreshState()

  if (refreshState.isRefreshing) {
    LaunchedEffect(Unit) {
      viewModel.onRefresh()
    }
  }

  LaunchedEffect(pageUiState.isRefreshing) {
    if (pageUiState.isRefreshing) {
      refreshState.startRefresh()
    } else {
      delay(1000)
      refreshState.endRefresh()
    }
  }

  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  val listItems = pageUiState.tabItems
  var columnHeightPx by remember {
    mutableIntStateOf(0)
  }

  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      viewModel.onRefresh()
      scope.launch {
        listState.scrollToItem(0)
      }
    }
  }

  LaunchedEffect(listState.canScrollBackward, listState.canScrollForward) {
    if (listState.canScrollBackward) {
      onSetBottomNavigationInterceptor(
        NavigationInterceptor.TargetRoute(
          targetRoute = "home",
          shouldIntercept = {
            listState.canScrollBackward
          },
          onIntercepted = {
            listState.animateScrollToItem(0)
            viewModel.onRefresh()
          },
        ),
      )
    } else {
      setParentBottomNavigationInterceptor()
    }
  }

  LaunchedEffect(onShouldInterceptTopNavigation(), listState.canScrollBackward, listState.canScrollForward) {
    if (!onShouldInterceptTopNavigation()) return@LaunchedEffect

    if (listState.canScrollBackward) {
      onSetTopNavigationInterceptor(
        NavigationInterceptor.TargetRoute(
          targetRoute = tabId,
          shouldIntercept = {
            listState.canScrollBackward && onShouldInterceptTopNavigation()
          },
          onIntercepted = {
            listState.animateScrollToItem(0)
            viewModel.onRefresh()
          },
        ),
      )
    } else {
      onSetTopNavigationInterceptor(NavigationInterceptor.None)
    }
  }

  Box(
    modifier =
      Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .nestedScroll(refreshState.nestedScrollConnection)
        .onGloballyPositioned {
          columnHeightPx = it.size.height
        },
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState,
    ) {
      itemsIndexed(items = listItems) { _, uiModel ->
        StorytellerItem(
          uiModel = uiModel,
          isRefreshing = pageUiState.isRefreshing || isRefreshing,
          navController = rootNavController,
          roundTheme = config?.roundTheme,
          squareTheme = config?.squareTheme,
        ) {
          viewModel.hideStorytellerItem(uiModel.itemId)
        }
      }
    }

    PullToRefreshContainer(
      modifier = Modifier.align(Alignment.TopCenter),
      state = refreshState,
    )
  }
}
