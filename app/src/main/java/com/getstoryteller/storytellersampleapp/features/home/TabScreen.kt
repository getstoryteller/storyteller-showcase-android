package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.features.main.MainViewModel
import com.getstoryteller.storytellersampleapp.features.main.bottomnav.NavigationInterceptor
import com.getstoryteller.storytellersampleapp.ui.PullToRefresh
import com.getstoryteller.storytellersampleapp.ui.StorytellerItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TabScreen(
  tabId: String,
  viewModel: TabViewModel,
  sharedViewModel: MainViewModel,
  rootNavController: NavController,
  isRefreshing: Boolean,
  config: Config?,
  onSetNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  setParentInterceptor: () -> Unit = {}
) {
  LaunchedEffect(key1 = tabId, block = {
    viewModel.loadTab(tabId)
  })

  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()

  val pageUiState by viewModel.uiState.collectAsState()
  val refreshState = rememberPullRefreshState(
    refreshing = pageUiState.isRefreshing,
    onRefresh = { viewModel.onRefresh() }
  )
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
      onSetNavigationInterceptor(
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
      setParentInterceptor()
    }
  }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colors.surface)
      .pullRefresh(
        state = refreshState
      )
      .onGloballyPositioned {
        columnHeightPx = it.size.height
      }
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState
    ) {
      itemsIndexed(items = listItems) { _, uiModel ->
        StorytellerItem(
          uiModel = uiModel,
          isRefreshing = pageUiState.isRefreshing || isRefreshing,
          navController = rootNavController,
          roundTheme = config?.roundTheme,
          squareTheme = config?.squareTheme
        )
      }
    }

    PullToRefresh(
      modifier = Modifier
        .align(Alignment.TopCenter),
      state = refreshState,
      refreshing = pageUiState.isRefreshing || isRefreshing
    )
  }
}
