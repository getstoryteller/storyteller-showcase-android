package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel
import com.example.storytellerSampleAndroid.compose.components.items.ChangeUserContainer
import com.example.storytellerSampleAndroid.compose.components.items.Header
import com.example.storytellerSampleAndroid.compose.components.items.NavigatedToApp
import com.example.storytellerSampleAndroid.compose.components.items.ToggleDarkModeContainer
import com.storyteller.data.StorytellerClipsDataModel
import com.storyteller.data.StorytellerStoriesDataModel
import com.storyteller.domain.entities.Error
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.StorytellerListViewStyle
import com.storyteller.ui.compose.components.lists.grid.StorytellerClipsGrid
import com.storyteller.ui.compose.components.lists.grid.StorytellerStoriesGrid
import com.storyteller.ui.compose.components.lists.row.StorytellerClipsRow
import com.storyteller.ui.compose.components.lists.row.StorytellerStoriesRow
import com.storyteller.ui.list.StorytellerListViewDelegate

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onRefresh: () -> Unit,
    viewModel: JetpackComposeViewModel,
    storytellerListViewDelegate: StorytellerListViewDelegate,
    onUserNavigatedToApp: (String) -> Unit,
    openSettings: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val refreshing by remember { viewModel.refreshing }
    val ptrState = rememberPullRefreshState(refreshing, onRefresh)
    val rotation = animateFloatAsState(ptrState.progress * 120)
    val darkMode by remember { viewModel.isDarkMode }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pullRefresh(state = ptrState)
    ) {

        var shouldShowRow by remember { mutableStateOf(true) }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            item {
                Header("Row View")
            }
            item {
                StorytellerStoriesRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    dataModel = StorytellerStoriesDataModel(
                        categories = listOf(),
                        uiStyle = if (darkMode) StorytellerListViewStyle.DARK else StorytellerListViewStyle.LIGHT,
                        cellType = StorytellerListViewCellType.SQUARE
                    ),
                    delegate = storytellerListViewDelegate,
                    isRefreshing = refreshing,
                )
            }
            item {
                Header("Grid View")
            }
            item {
                StorytellerStoriesGrid(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight(),
                    dataModel = StorytellerStoriesDataModel(
                        categories = listOf(),
                        displayLimit = 4,
                        uiStyle = if (darkMode) StorytellerListViewStyle.DARK else StorytellerListViewStyle.LIGHT,
                        cellType = StorytellerListViewCellType.SQUARE
                    ),
                    isScrollable = false,
                    delegate = storytellerListViewDelegate,
                    isRefreshing = refreshing,
                )
            }
            item {
                Header("Clips Row View")
            }
            item {
                StorytellerClipsRow(
                    modifier = Modifier.fillMaxWidth(),
                    dataModel = StorytellerClipsDataModel(
                        collection = "clips_row",
                        uiStyle = if (darkMode) StorytellerListViewStyle.DARK else StorytellerListViewStyle.LIGHT,
                        cellType = StorytellerListViewCellType.SQUARE
                    ),
                    delegate = storytellerListViewDelegate,
                    isRefreshing = refreshing,
                )
            }
            item {
                Header("Clips Grid View")
            }
            item {
                StorytellerClipsGrid(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight(),
                    dataModel = StorytellerClipsDataModel(
                        collection = "clips_grid",
                        displayLimit = 4,
                        uiStyle = if (darkMode) StorytellerListViewStyle.DARK else StorytellerListViewStyle.LIGHT,
                        cellType = StorytellerListViewCellType.SQUARE
                    ),
                    delegate = storytellerListViewDelegate,
                    isRefreshing = refreshing,
                    isScrollable = false
                )
            }

            item {
                AnimatedVisibility(visible = shouldShowRow) {
                    StorytellerClipsGrid(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight(),
                        dataModel = StorytellerClipsDataModel(
                            collection = "clips_grid",
                            displayLimit = 4,
                            uiStyle = if (darkMode) StorytellerListViewStyle.DARK else StorytellerListViewStyle.LIGHT,
                            cellType = StorytellerListViewCellType.SQUARE
                        ),
                        delegate = storytellerListViewDelegate,
                        isRefreshing = refreshing,
                        isScrollable = false
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = openSettings) {
                        Text(text = "Open Settings")
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val userNavigatedToApp by viewModel.userNavigatedToApp
                        .collectAsStateWithLifecycle(initialValue = "")
                    NavigatedToApp(
                        text = userNavigatedToApp,
                        onUserNavigatedToApp = onUserNavigatedToApp
                    )
                }
            }

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

interface EmptyStorytellerDelegate : StorytellerListViewDelegate {
    override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) = Unit

    override fun onDataLoadStarted() = Unit

    override fun onPlayerDismissed() = Unit

}

fun hasDataStorytellerDelegate(callback: (Boolean) -> Unit) = object : EmptyStorytellerDelegate {
    override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        callback(dataCount > 0)
    }
}
