package com.getstoryteller.storytellershowcaseapp.ui.components.pullrefresh

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
@ExperimentalMaterial3Api
fun rememberStorytellerPullToRefreshState(
    positionalThreshold: Dp = PullToRefreshDefaults.PositionalThreshold,
    enabled: () -> Boolean = { true },
): PullToRefreshState {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val positionalThresholdPx = with(density) { positionalThreshold.toPx() }
    return rememberSaveable(
        positionalThresholdPx, enabled,
        saver = StorytellerPullToRefreshStateImpl.Saver(
            coroutineScope = coroutineScope,
            positionalThreshold = positionalThresholdPx,
            enabled = enabled,
        )
    ) {
        StorytellerPullToRefreshStateImpl(
            coroutineScope = coroutineScope,
            initialRefreshing = false,
            positionalThreshold = positionalThresholdPx,
            enabled = enabled,
        )
    }
}
