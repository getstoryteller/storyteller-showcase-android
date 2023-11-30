package com.getstoryteller.storytellersampleapp.features.watch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentManager
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.ui.StorytellerClipsFragmentComponent

@Composable
fun MomentsScreen(
  modifier: Modifier,
  viewModel: MomentsViewModel,
  fragmentManager: FragmentManager,
  config: Config?
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        StorytellerClipsFragmentComponent(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            fragmentManager = fragmentManager,
            collectionId = config?.topLevelCollectionId ?: ""
        )
    }
}
