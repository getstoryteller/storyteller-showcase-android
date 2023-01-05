package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel
import com.example.storytellerSampleAndroid.compose.components.items.ChangeUserContainer
import com.example.storytellerSampleAndroid.compose.components.items.Header
import com.storyteller.domain.StorytellerListViewCellType
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.sdk.compose.StorytellerGridView
import com.storyteller.sdk.compose.StorytellerRowView
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

  Box(
    modifier = modifier
      .fillMaxSize()
      .padding(paddingValues)
      .pullRefresh(state = ptrState)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState
    ) {
      item {
        Header("Row View")
      }
      item {
        StorytellerRowView(
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
      item {
        Header("Grid View")
      }
      item {
        StorytellerGridView(
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

      item { ChangeUserContainer(onRefresh, coroutineScope, listState, viewModel) }
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





