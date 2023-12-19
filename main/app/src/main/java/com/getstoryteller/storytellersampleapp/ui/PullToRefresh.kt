package com.getstoryteller.storytellersampleapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefresh(
    state: PullRefreshState,
    refreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(40.dp)
            .pullRefreshIndicatorTransform(state)
            .rotate(state.progress * 120f),
        shape = RoundedCornerShape(10.dp),
        color = Color.DarkGray,
        tonalElevation = if (state.progress > 0 || refreshing) 20.dp else 0.dp
    ) {
        Box {
            if (refreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(25.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(25.dp),
                    color = Color.White,
                    strokeWidth = 3.dp,
                    progress = state.progress
                )
            }
        }
    }
}
