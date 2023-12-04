package com.getstoryteller.storytellersampleapp.features.watch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.ui.StorytellerEmbeddedClips
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun MomentsScreen(
  modifier: Modifier,
  config: Config?,
  onCommit: (fragment: Fragment, tag: String) -> FragmentTransaction.(containerId: Int) -> Unit,
  tag: String,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Red),
  ) {
    StorytellerEmbeddedClips(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
      onCommit = onCommit(
        StorytellerClipsFragment.create(config?.topLevelCollectionId ?: ""),
        tag,
      ),
    )
  }
}
