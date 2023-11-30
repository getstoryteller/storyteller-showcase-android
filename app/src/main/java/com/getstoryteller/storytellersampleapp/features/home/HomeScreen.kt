package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.ui.StorytellerItem
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  config: Config?,
  navController: NavController,
  isRefreshing: Boolean
) {
  LaunchedEffect(key1 = config?.configId ?: UUID.randomUUID().toString(), block = {
    config?.let {
      viewModel.loadHomePage(it)
    }
  })

  val pageUiState by viewModel.uiState.collectAsState()
  val refreshState = rememberPullRefreshState(
    refreshing = pageUiState.isRefreshing,
    onRefresh = { viewModel.onRefresh() }
  )
  val listState = rememberLazyListState()
  val listItems = pageUiState.homeItems
  var columnHeightPx by remember {
    mutableIntStateOf(0)
  }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .pullRefresh(
        state = refreshState
      )
      .onGloballyPositioned {
        columnHeightPx = it.size.height
      }
  ) {
    if (!pageUiState.tabsEnabled) {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
      ) {
        itemsIndexed(items = listItems) { _, uiModel ->
          StorytellerItem(
            uiModel = uiModel,
            isRefreshing = pageUiState.isRefreshing || isRefreshing,
            navController = navController
          )
        }
      }
    } else {
      TabLayout(
        tabs = pageUiState.tabs,
        rootNavController = navController,
        isRefreshing = isRefreshing
      )
    }
  }
}
