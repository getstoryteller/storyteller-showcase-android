package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.LayoutType
import com.getstoryteller.storytellersampleapp.data.TileType
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
      .padding(12.dp)
  ) {
    StorytellerItem(
      uiModel = pageItemUiModel.copy(
        tileType = TileType.RECTANGULAR,
        layout = LayoutType.GRID
      ),
      isRefreshing = true,
      navController = navController,
      disableHeader = true,
      squareTheme = config?.squareTheme,
      roundTheme = config?.roundTheme
    )
  }
}
