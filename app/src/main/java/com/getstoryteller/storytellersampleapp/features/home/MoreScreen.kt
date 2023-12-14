package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.LayoutType
import com.getstoryteller.storytellersampleapp.data.TileType
import com.getstoryteller.storytellersampleapp.data.entities.TileType
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.ui.StorytellerItem

@Composable
fun MoreScreen(
  pageItemUiModel: PageItemUiModel,
  viewModel: MoreViewModel,
  navController: NavController,
  config: Config?
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    StorytellerItem(
      uiModel = pageItemUiModel.copy(
        tileType = TileType.RECTANGULAR,
        layout = LayoutType.GRID
      ),
      isRefreshing = true,
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
    )
  }
}
