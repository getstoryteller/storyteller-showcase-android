package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.storyteller.domain.StorytellerListViewCellType
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.sdk.compose.StorytellerRowView
import com.storyteller.ui.list.StorytellerListViewDelegate

@Composable
fun RowItem(
  modifier: Modifier = Modifier,
  tag: String,
  categories: List<String> = listOf(),
  controller: StorytellerComposeController,
  delegate: StorytellerListViewDelegate
) {
  StorytellerRowView(modifier = modifier, tag = tag, controller = controller) {
    this.delegate = delegate
    cellType = StorytellerListViewCellType.SQUARE
    this.categories = categories
    reloadData()
  }
}

