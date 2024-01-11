package com.getstoryteller.storytellershowcaseapp.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.remote.entities.LayoutType
import com.getstoryteller.storytellershowcaseapp.remote.entities.TileType
import com.getstoryteller.storytellershowcaseapp.ui.components.pullrefresh.rememberStorytellerPullToRefreshState
import com.getstoryteller.storytellershowcaseapp.ui.features.storyteller.StorytellerItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
  modifier: Modifier = Modifier,
  pageItemUiModel: PageItemUiModel,
  navController: NavController,
  config: Config?,
) {
  var isRefreshing by remember(pageItemUiModel) { mutableStateOf(false) }
  val refreshState = rememberStorytellerPullToRefreshState()

  if (refreshState.isRefreshing) {
    LaunchedEffect(true) {
      isRefreshing = true
    }
  }

  Box(
    modifier =
    modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .nestedScroll(refreshState.nestedScrollConnection)
      .padding(bottom = 16.dp),
  ) {
    StorytellerItem(
      uiModel =
      pageItemUiModel.copy(
        tileType = TileType.RECTANGULAR,
        layout = LayoutType.GRID,
      ),
      isRefreshing = isRefreshing,
      navController = navController,
      roundTheme = config?.roundTheme,
      squareTheme =
      config?.squareTheme?.let {
        it.copy(
          light =
          it.light.copy(
            lists =
            it.light.lists.copy(
              grid =
              it.light.lists.grid.copy(
                topInset = 12,
              ),
            ),
          ),
          dark =
          it.dark.copy(
            lists =
            it.dark.lists.copy(
              grid =
              it.dark.lists.grid.copy(
                topInset = 12,
              ),
            ),
          ),
        )
      },
      disableHeader = true,
      isScrollable = true,
      onShouldHide = {
        refreshState.endRefresh()
        isRefreshing = false
      },
    )

    PullToRefreshContainer(
      modifier = Modifier.align(Alignment.TopCenter),
      state = refreshState,
    )
  }
}
