package com.example.storytellerSampleAndroid.compose.components.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.storyteller.domain.StorytellerListViewCellType
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.sdk.compose.StorytellerGridView
import com.storyteller.ui.list.StorytellerListViewDelegate

@Composable
fun GridItem(
  modifier: Modifier = Modifier,
  tag: String,
  categories: List<String> = listOf(),
  controller: StorytellerComposeController,
  delegate: StorytellerListViewDelegate
) {
  StorytellerGridView(modifier = modifier, tag = tag, controller = controller) {
    this.delegate = delegate
    cellType = StorytellerListViewCellType.ROUND
    this.categories = categories
    reloadData()
  }
}