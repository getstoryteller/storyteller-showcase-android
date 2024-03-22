package com.getstoryteller.storytellershowcaseapp.ui.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.remote.entities.TabDto
import com.getstoryteller.storytellershowcaseapp.ui.components.StorytellerScrollableTabRow
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.main.bottomnavigation.NavigationInterceptor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TabLayout(
  rootNavController: NavController,
  parentState: TabLayoutUiState,
  sharedViewModel: MainViewModel,
  onSetNavigationInterceptor: (NavigationInterceptor) -> Unit = {},
  onLocationChanged: (String) -> Unit,
) {
  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()
  var reloadCurrentTabSignal by remember { mutableIntStateOf(-1) }
  val tabs = remember(parentState.tabs) { parentState.tabs }

  val pagerState = rememberPagerState(pageCount = { tabs.size })
  val scope = rememberCoroutineScope()
  val currentPage = remember(tabs) { derivedStateOf { pagerState.currentPage } }
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      scope.launch {
        pagerState.animateScrollToPage(0)
      }
    }
  }

  var topBarNavigationInterceptor: NavigationInterceptor by remember {
    mutableStateOf(NavigationInterceptor.None)
  }

  Column(modifier = Modifier.fillMaxSize()) {
    StorytellerScrollableTabRow(
      modifier = Modifier.zIndex(1F),
      selectedTabIndex = currentPage.value,
      backgroundColor = MaterialTheme.colorScheme.surface,
      contentColor = Color.fromHex("#ffcd07"),
      edgePadding = 0.dp,
      divider = {},
    ) {
      tabs.forEachIndexed { index, tab ->
        val isSelected by remember(currentPage.value) {
          mutableStateOf(index == currentPage.value)
        }

        LaunchedEffect(key1 = isSelected) {
          if (isSelected) {
            onLocationChanged(tab.name)
          }
        }

        Tab(text = {
          Text(
            modifier = Modifier.wrapContentWidth(),
            text = tab.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600,
            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface,
          )
        }, selected = isSelected, onClick = {
          val interceptor = topBarNavigationInterceptor
          if (interceptor is NavigationInterceptor.TargetRoute && isSelected && interceptor.shouldIntercept()) {
            coroutineScope.launch {
              interceptor.onIntercepted()
            }
            return@Tab
          } else if (interceptor is NavigationInterceptor.None && isSelected) {
            reloadCurrentTabSignal = index
          }
          scope.launch {
            pagerState.animateScrollToPage(index)
          }
        })
      }
    }

    HorizontalPager(
      state = pagerState,
      // This needs to be zero or nav interception will not work
      beyondBoundsPageCount = 0,
      key = { tabs[it].name },
    ) { pageIndex ->
      val tabValue = remember(tabs.hashCode(), pageIndex) { tabs[pageIndex].value }
      val viewModel: TabViewModel = hiltViewModel<TabViewModel>(key = "${tabs.hashCode()}, $pageIndex")

      LaunchedEffect(tabs.hashCode(), pageIndex) {
        viewModel.loadTab(tabValue)
      }

      LaunchedEffect(reloadCurrentTabSignal) {
        if (reloadCurrentTabSignal == pageIndex) {
          viewModel.onRefresh()
          reloadCurrentTabSignal = -1
        }
      }

      TabScreen(
        tabId = tabValue,
        tabViewModel = viewModel,
        sharedViewModel = sharedViewModel,
        rootNavController = rootNavController,
        isRefreshing = parentState.isRefreshing,
        config = parentState.config,
        onShouldInterceptTopNavigation = { pagerState.currentPage == pageIndex },
        onSetTopNavigationInterceptor = {
          topBarNavigationInterceptor = it
        },
        onSetBottomNavigationInterceptor = onSetNavigationInterceptor,
        setParentBottomNavigationInterceptor = {
          if (pagerState.currentPage == pageIndex) {
            onSetNavigationInterceptor(
              NavigationInterceptor.TargetRoute(
                targetRoute = "home",
                shouldIntercept = { true },
                onIntercepted = {
                  coroutineScope.launch {
                    pagerState.scrollToPage(0)
                    reloadCurrentTabSignal = 0
                  }
                },
              ),
            )
          }
        },
      )
    }
  }
}

@Stable
data class TabLayoutUiState(
  val tabs: List<TabDto>,
  val isRefreshing: Boolean,
  val config: Config?,
)

fun Color.Companion.fromHex(
  hexString: String,
) = Color(android.graphics.Color.parseColor(hexString))
