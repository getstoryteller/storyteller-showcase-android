package com.getstoryteller.storytellershowcaseapp.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.remote.entities.LayoutType.ROW
import com.getstoryteller.storytellershowcaseapp.ui.components.pullrefresh.rememberStorytellerPullToRefreshState
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.NavigationInterceptor
import com.getstoryteller.storytellershowcaseapp.ui.features.storyteller.StorytellerItem
import com.storyteller.ui.compose.components.lists.grid.rememberStorytellerGridState
import com.storyteller.ui.compose.components.lists.row.StorytellerDataState
import com.storyteller.ui.compose.components.lists.row.rememberStorytellerRowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(
  tabId: String,
  tabViewModel: TabViewModel,
  sharedViewModel: MainViewModel,
  rootNavController: NavController,
  config: Config?,
  onSetTopNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  onShouldInterceptTopNavigation: () -> Boolean = { false },
  onSetBottomNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  setParentBottomNavigationInterceptor: () -> Unit = {},
) {
  LaunchedEffect(
    key1 = tabId,
    block = {
      tabViewModel.loadTab(tabId)
    },
  )

  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()

  val pageUiState by tabViewModel.uiState.collectAsState()
  val itemIds = remember(pageUiState) { pageUiState.tabs.map { it.itemId } }
  val reloadStates = remember(itemIds) { mutableStateMapOf<String, Boolean>() }

  val innerListStates = remember { mutableStateMapOf<String, StorytellerDataState>() }
  val refreshState = rememberStorytellerPullToRefreshState()
  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  var columnHeightPx by remember { mutableIntStateOf(0) }

  val items = remember(pageUiState) { pageUiState.tabs.toList() }

  fun reloadItem(
    itemId: String? = null,
  ) {
    tabViewModel.onRefresh()
    if (itemId != null) {
      reloadStates[itemId] = true
    } else {
      reloadStates.forEach { (t, _) ->
        reloadStates[t] = true
      }
    }
  }

  if (refreshState.isRefreshing) {
    LaunchedEffect(Unit) {
      reloadItem()
    }
  }

  LaunchedEffect(itemIds) {
    (itemIds - reloadStates.keys).forEach {
      reloadStates[it] = false
    }
  }

  LaunchedEffect(pageUiState.isRefreshing) {
    if (pageUiState.isRefreshing) {
      refreshState.startRefresh()
    } else {
      delay(1000)
      refreshState.endRefresh()
    }
  }

  val tabReloadTimeout by tabViewModel.tabReloadTimeout.collectAsState()
  LaunchedEffect(tabReloadTimeout) {
    if (tabReloadTimeout) {
      tabViewModel.tabDisposed()
      tabViewModel.onRefresh()
      listState.animateScrollToItem(0)
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      tabViewModel.tabDisposed()
    }
  }

  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      reloadItem()
      scope.launch {
        listState.scrollToItem(0)
      }
    }
  }

  LaunchedEffect(listState.canScrollBackward, listState.canScrollForward) {
    if (listState.canScrollBackward) {
      onSetBottomNavigationInterceptor(
        NavigationInterceptor.TargetRoute(
          targetRoute = "home",
          shouldIntercept = {
            listState.canScrollBackward
          },
          onIntercepted = {
            listState.animateScrollToItem(0)
            reloadItem()
          },
        ),
      )
    } else {
      setParentBottomNavigationInterceptor()
    }
  }

  LaunchedEffect(onShouldInterceptTopNavigation(), listState.canScrollBackward, listState.canScrollForward) {
    if (!onShouldInterceptTopNavigation()) return@LaunchedEffect

    if (listState.canScrollBackward) {
      onSetTopNavigationInterceptor(
        NavigationInterceptor.TargetRoute(
          targetRoute = tabId,
          shouldIntercept = {
            listState.canScrollBackward && onShouldInterceptTopNavigation()
          },
          onIntercepted = {
            listState.animateScrollToItem(0)
            reloadItem()
          },
        ),
      )
    } else {
      onSetTopNavigationInterceptor(NavigationInterceptor.None)
    }
  }

  Box(
    modifier =
    Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colorScheme.surface)
      .nestedScroll(refreshState.nestedScrollConnection)
      .onGloballyPositioned {
        columnHeightPx = it.size.height
      },
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState,
    ) {
      itemsIndexed(items = items) { _, item ->
        if (item.isHidden && !reloadStates.keys.contains(item.itemId)) return@itemsIndexed
        val innerListState = innerListStates.getOrPut(item.itemId) {
          if (item.layout == ROW) {
            rememberStorytellerRowState(item.itemId)
          } else {
            rememberStorytellerGridState(item.itemId)
          }
        }
        val reloadState = reloadStates[item.itemId] ?: false
        LaunchedEffect(reloadState) {
          if (reloadState) {
            Timber.d("Reloading item $item")
            innerListState.reloadData()
          }
        }
        StorytellerItem(
          uiModel = item,
          navController = rootNavController,
          roundTheme = config?.roundTheme,
          squareTheme = config?.squareTheme,
          state = innerListState,
          onDataLoadComplete = {
            reloadStates[item.itemId] = false
            Timber.d("Data loaded for item $item")
          },
          onShouldHide = {
            Timber.d("Hiding item $item, because of $it")
            reloadStates[item.itemId] = false
            tabViewModel.hideStorytellerItem(item.itemId)
          },
        )
      }
    }

    PullToRefreshContainer(
      modifier = Modifier.align(Alignment.TopCenter),
      state = refreshState,
    )
  }
}
