package com.getstoryteller.storytellershowcaseapp.ui.components.pullrefresh

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow

@ExperimentalMaterial3Api
internal class StorytellerPullToRefreshStateImpl(
    private val coroutineScope: CoroutineScope,
    initialRefreshing: Boolean,
    override val positionalThreshold: Float,
    enabled: () -> Boolean,
) : PullToRefreshState {

    override val progress get() = adjustedDistancePulled / positionalThreshold
    override val verticalOffset get() = _verticalOffset

    override val isRefreshing get() = _refreshing

    override fun startRefresh() {
        _refreshing = true
        _verticalOffset = positionalThreshold
    }

    override fun endRefresh() {
        _refreshing = false
        coroutineScope.launch {
            animateTo(0f)
        }
    }

    override var nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource,
        ): Offset = when {
            !enabled() -> Offset.Zero
            // Swiping up
            source == NestedScrollSource.Drag && available.y < 0 -> {
                consumeAvailableOffset(available)
            }

            else -> Offset.Zero
        }

        override fun onPostScroll(
            consumed: Offset, available: Offset, source: NestedScrollSource
        ): Offset = when {
            !enabled() -> Offset.Zero
            // Swiping down
            source == NestedScrollSource.Drag && available.y > 0 -> {
                consumeAvailableOffset(available)
            }

            else -> Offset.Zero
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            return Velocity(0f, onRelease(available.y))
        }
    }

    /** Helper method for nested scroll connection */
    fun consumeAvailableOffset(available: Offset): Offset {
        val y = if (isRefreshing) 0f else {
            val newOffset = (distancePulled + available.y).coerceAtLeast(0f)
            val dragConsumed = newOffset - distancePulled
            distancePulled = newOffset
            val calculateVerticalOffset = calculateVerticalOffset()
            Log.d("ManoO", "consumeAvailableOffset: $calculateVerticalOffset")
            _verticalOffset = calculateVerticalOffset
            dragConsumed
        }
        return Offset(0f, y)
    }

    /** Helper method for nested scroll connection. Calls onRefresh callback when triggered */
    suspend fun onRelease(velocity: Float): Float {
        if (isRefreshing) return 0f // Already refreshing, do nothing
        // Trigger refresh
        if (adjustedDistancePulled > positionalThreshold) {
            startRefresh()
        } else {
            // Not enough drag, animate back to 0
            animateTo(0f)
        }

        val consumed = when {
            // We are flinging without having dragged the pull refresh (for example a fling inside
            // a list) - don't consume
            distancePulled == 0f -> 0f
            // If the velocity is negative, the fling is upwards, and we don't want to prevent the
            // the list from scrolling
            velocity < 0f -> 0f
            // We are showing the indicator, and the fling is downwards - consume everything
            else -> velocity
        }
        distancePulled = 0f
        return consumed
    }

    private suspend fun animateTo(offset: Float) {
        animate(
            initialValue = _verticalOffset,
            targetValue = offset,
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = Spring.DampingRatioMediumBouncy,
                visibilityThreshold = 16.dp.value,
            )
        ) { value, _ ->
            _verticalOffset = value
        }
    }

    /** Provides custom vertical offset behavior for [PullToRefreshContainer] */
    private fun calculateVerticalOffset(): Float = when {
        // If drag hasn't gone past the threshold, the position is the adjustedDistancePulled.
        adjustedDistancePulled <= positionalThreshold -> adjustedDistancePulled
        else -> {
            // How far beyond the threshold pull has gone, as a percentage of the threshold.
            val overshootPercent = abs(progress) - 1.0f
            // Limit the overshoot to 200%. Linear between 0 and 200.
            val linearTension = overshootPercent.coerceIn(0f, 2f)
            // Non-linear tension. Increases with linearTension, but at a decreasing rate.
            val tensionPercent = linearTension - linearTension.pow(2) / 4
            // The additional offset beyond the threshold.
            val extraOffset = positionalThreshold * tensionPercent
            positionalThreshold + extraOffset
        }
    }

    companion object {
        /** The default [Saver] for [StorytellerPullToRefreshStateImpl]. */
        fun Saver(
            coroutineScope: CoroutineScope,
            positionalThreshold: Float,
            enabled: () -> Boolean,
        ) = Saver<PullToRefreshState, Boolean>(save = { it.isRefreshing },
            restore = { isRefreshing ->
                StorytellerPullToRefreshStateImpl(coroutineScope, isRefreshing, positionalThreshold, enabled)
            })
    }

    private var distancePulled by mutableFloatStateOf(0f)
    private val adjustedDistancePulled: Float get() = distancePulled * DragMultiplier
    private var _verticalOffset by mutableFloatStateOf(0f)
    private var _refreshing by mutableStateOf(initialRefreshing)
}

private const val DragMultiplier = 0.5f
