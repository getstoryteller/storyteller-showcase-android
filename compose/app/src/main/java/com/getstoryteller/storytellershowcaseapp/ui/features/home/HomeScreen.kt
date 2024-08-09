package com.getstoryteller.storytellershowcaseapp.ui.features.home

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.remote.entities.LayoutType.ROW
import com.getstoryteller.storytellershowcaseapp.ui.components.NoContent
import com.getstoryteller.storytellershowcaseapp.ui.components.pullrefresh.rememberStorytellerPullToRefreshState
import com.getstoryteller.storytellershowcaseapp.ui.features.DeeplinkHandler
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.NavigationInterceptor
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.popUpTo
import com.getstoryteller.storytellershowcaseapp.ui.features.storyteller.StorytellerItem
import com.storyteller.ui.compose.components.lists.grid.rememberStorytellerGridState
import com.storyteller.ui.compose.components.lists.row.StorytellerDataState
import com.storyteller.ui.compose.components.lists.row.rememberStorytellerRowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  sharedViewModel: MainViewModel,
  deepLinkData: String?,
  config: Config?,
  navController: NavController,
  isRefreshing: Boolean,
  modifier: Modifier = Modifier,
  onSetNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  onNavigateToLogin: () -> Unit = {},
  onLocationChanged: (String) -> Unit,
  isHomeRefreshing: (Boolean) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()
  val pageUiState by viewModel.uiState.collectAsState()
  val loginState by sharedViewModel.loginUiState.collectAsState()
  val innerListStates = remember { mutableStateMapOf<String, StorytellerDataState>() }
  val itemIds = remember(pageUiState) { pageUiState.homeItems.map { it.itemId } }
  val reloadStates = remember(itemIds) { mutableStateMapOf<String, Any?>() }
  val refreshState = rememberStorytellerPullToRefreshState()
  val context = LocalContext.current

  val listState = rememberLazyListState()
  val listItems = pageUiState.homeItems
  var columnHeightPx by remember {
    mutableIntStateOf(0)
  }
  if (refreshState.isRefreshing) {
    LaunchedEffect(true) {
      scope.launch {
        sharedViewModel.refreshMainPage()
      }
    }
  }

  LaunchedEffect(pageUiState.homeItems) {
    if (pageUiState.homeItems.isEmpty() || deepLinkData == null) return@LaunchedEffect
    if (sharedViewModel.handledDeepLink == deepLinkData) return@LaunchedEffect
    DeeplinkHandler.handleDeepLink(context as Activity, deeplink = deepLinkData) { destination ->
      sharedViewModel.handleDeeplink(deepLinkData)
      navController.popUpTo(destination)
    }
  }

  fun reloadData(
    config: Config?,
  ) {
    config?.also(viewModel::loadHomePage)
    innerListStates.values.forEach { listState ->
      scope.launch { listState.reloadData() }
    }
  }
  LaunchedEffect(key1 = pageUiState.isRefreshing) {
    isHomeRefreshing(pageUiState.isRefreshing)
    if (pageUiState.isRefreshing) {
      refreshState.startRefresh()
    } else {
      delay(1000L)
      refreshState.endRefresh()
    }
  }

  LaunchedEffect(
    key1 = config?.configId ?: UUID.randomUUID().toString(),
    block = {
      config?.let {
        reloadData(it)
      }
    },
  )
  LaunchedEffect(loginState.isLoggedIn, config) {
    if (!loginState.isLoggedIn && config == null) {
      onNavigateToLogin()
    }
  }
  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      config?.let {
        reloadData(it)
      }
    }
  }
  Box(
    modifier =
    modifier
      .fillMaxSize()
      .nestedScroll(refreshState.nestedScrollConnection)
      .onGloballyPositioned {
        columnHeightPx = it.size.height
      },
  ) {
    if (pageUiState.noContentAvailable) {
      NoContent()
      PullToRefreshContainer(
        modifier = Modifier.align(Alignment.TopCenter),
        state = refreshState,
      )
    } else {
      if (!pageUiState.tabsEnabled) {
        LaunchedEffect(key1 = Unit) {
          onLocationChanged("Home")
        }
        LazyColumn(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          state = listState,
        ) {
          items(items = listItems) { item ->
            when (item) {
              is VideoItemUiModel -> {
                val rowState = rememberStorytellerRowState(item.itemId)
                val gridState = rememberStorytellerGridState(item.itemId)

                val state = remember(item.itemId) {
                  innerListStates.getOrPut(item.itemId) { if (item.layout == ROW) rowState else gridState }
                }

                val reloadState = reloadStates[item.itemId]
                LaunchedEffect(reloadState) {
                  if (reloadState != null) {
                    val tag = if (item.categories.isNotEmpty()) item.categories.first() else item.collectionId
                    Timber.d("[$tag] Reloading item $item")
                    state.reloadData()
                  }
                }

                StorytellerItem(
                  uiModel = item,
                  navController = navController,
                  roundTheme = config?.roundTheme,
                  squareTheme = config?.squareTheme,
                  state = state,
                  onDataLoadComplete = {
                    reloadStates[item.itemId] = null
                  },
                )
              }

              is ImageItemUiModel -> {
                ImageActionItem(uiModel = item)
              }
            }
          }
        }
        PullToRefreshContainer(
          modifier = Modifier.align(Alignment.TopCenter),
          state = refreshState,
        )
      } else {
        TabLayout(
          rootNavController = navController,
          sharedViewModel = sharedViewModel,
          parentState =
          TabLayoutUiState(
            tabs = pageUiState.tabs,
            isRefreshing = pageUiState.isRefreshing || isRefreshing,
            config = config,
          ),
          onSetNavigationInterceptor = onSetNavigationInterceptor,
          onLocationChanged = onLocationChanged,
        )
      }
    }
  }
}
