package com.getstoryteller.storytellersampleapp.features.watch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.fragment.app.FragmentManager
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.ui.StorytellerClipsFragmentComponent

@Composable
fun WatchScreen(
    viewModel: WatchViewModel,
    fragmentManager: FragmentManager,
    config: Config?
) {
    var columnHeightPx by remember {
        mutableStateOf(0)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .onGloballyPositioned {
                columnHeightPx = it.size.height
            }
    ) {
        StorytellerClipsFragmentComponent(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            fragmentManager = fragmentManager,
            collectionId = config?.topLevelCollectionId ?: "",
            height = columnHeightPx
        )
    }
}