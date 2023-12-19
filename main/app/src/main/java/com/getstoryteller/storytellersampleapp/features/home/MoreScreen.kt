package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.entities.LayoutType
import com.getstoryteller.storytellersampleapp.data.entities.TileType
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.ui.PullToRefresh
import com.getstoryteller.storytellersampleapp.ui.StorytellerItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoreScreen(
  pageItemUiModel: PageItemUiModel,
  navController: NavController,
  config: Config?
) {

  var isRefreshing by remember(pageItemUiModel) { mutableStateOf(false) }
  val refreshState = rememberPullRefreshState(
    refreshing = isRefreshing,
    onRefresh = {
      isRefreshing = true
    }
  )
  Box(
    modifier = Modifier
      .fillMaxSize()
      .pullRefresh(refreshState)
      .navigationBarsPadding()
      .padding(bottom = 16.dp)
  ) {
    StorytellerItem(
      uiModel = pageItemUiModel.copy(
        tileType = TileType.RECTANGULAR,
        layout = LayoutType.GRID
      ),
      isRefreshing = isRefreshing,
      navController = navController,
      roundTheme = config?.roundTheme,
      squareTheme = config?.squareTheme?.let {
        it.copy(
          light = it.light.copy(
            lists = it.light.lists.copy(
              grid = it.light.lists.grid.copy(
                topInset = 12
              )
            )
          ),
          dark = it.dark.copy(
            lists = it.dark.lists.copy(
              grid = it.dark.lists.grid.copy(
                topInset = 12
              )
            )
          )
        )
      },
      disableHeader = true,
      isScrollable = true,
      onComplete = {
        isRefreshing = false
      }
    )

    PullToRefresh(
      modifier = Modifier
        .align(Alignment.TopCenter),
      state = refreshState,
      refreshing = isRefreshing
    )
  }
}
