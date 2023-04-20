package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel
import com.example.storytellerSampleAndroid.compose.components.items.Banner
import com.example.storytellerSampleAndroid.compose.components.items.ChangeUserContainer
import com.example.storytellerSampleAndroid.compose.components.items.Header
import com.example.storytellerSampleAndroid.compose.components.items.ToggleDarkModeContainer
import com.example.storytellerSampleAndroid.compose.theme.random
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.StorytellerListViewStyle
import com.storyteller.sdk.compose.StorytellerClipsGridView
import com.storyteller.sdk.compose.StorytellerClipsRowView
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.sdk.compose.StorytellerStoriesGridView
import com.storyteller.sdk.compose.StorytellerStoriesRowView
import com.storyteller.ui.list.StorytellerListViewDelegate

@Composable
fun MainContent(
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues,
  onRefresh: () -> Unit,
  viewModel: JetpackComposeViewModel,
  controller: StorytellerComposeController,
  storytellerListViewDelegate: StorytellerListViewDelegate
) {
  val coroutineScope = rememberCoroutineScope()
  val listState = rememberLazyListState()

  val refreshing by remember { viewModel.refreshing }
  val ptrState = rememberPullRefreshState(refreshing, onRefresh)
  val rotation = animateFloatAsState(ptrState.progress * 120)
  val darkMode by remember { viewModel.isDarkMode }

  val randomColor1 = remember { Color.random() }
  val randomColor2 = remember { Color.random() }
  val randomColor3 = remember { Color.random() }
  val randomColor4 = remember { Color.random() }

  Box(
    modifier = modifier
      .fillMaxSize()
      .padding(paddingValues)
      .pullRefresh(state = ptrState)
  ) {
    LaunchedEffect(key1 = darkMode) {
      val uiStyle = if (darkMode) {
        StorytellerListViewStyle.DARK
      } else {
        StorytellerListViewStyle.LIGHT
      }
      controller.forEach { item ->
        item.uiStyle = uiStyle
      }
    }
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState
    ) {
      item { Header("Row View") }
      item {
        StorytellerStoriesRowView(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
          tag = "row",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          cellType = StorytellerListViewCellType.SQUARE
          categories = listOf()
          reloadData()
        }
      }
      item { Banner(randomColor1) }
      item { Header("Grid View") }
      item {
        StorytellerStoriesGridView(
          modifier = Modifier.fillMaxWidth(),
          tag = "grid",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          cellType = StorytellerListViewCellType.ROUND
          categories = listOf()
          reloadData()
        }
      }
      item { Banner(randomColor2) }
      item { Header("Clips Row View") }
      item {
        StorytellerClipsRowView(
          modifier = Modifier.fillMaxWidth(),
          tag = "clips_row",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          cellType = StorytellerListViewCellType.SQUARE
          collection = "clipssample"
          reloadData()
        }
      }
      item { Banner(randomColor3) }
      item { Header("Clips Grid View") }
      item {
        StorytellerClipsGridView(
          modifier = Modifier.fillMaxWidth(),
          tag = "clips_grid",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          cellType = StorytellerListViewCellType.SQUARE
          collection = "clipssample"
          reloadData()
        }
      }
      item { Banner(randomColor4) }
      item {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          ChangeUserContainer(onRefresh, coroutineScope, listState, viewModel)
          ToggleDarkModeContainer {
            viewModel.toggleDarkMode()
          }
        }
      }
    }

    PullToRefresh(
      modifier = Modifier
        .align(Alignment.TopCenter),
      state = ptrState,
      rotation = rotation.value,
      refreshing = refreshing
    )
  }
}

