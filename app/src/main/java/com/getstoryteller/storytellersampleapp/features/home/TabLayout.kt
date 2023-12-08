package com.getstoryteller.storytellersampleapp.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.TabDto
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.features.main.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TabLayout(
  rootNavController: NavController,
  state: TabLayoutUiState,
  sharedViewModel: MainViewModel
) {
  val reloadDataTrigger by sharedViewModel.reloadHomeTrigger.observeAsState()

  val tabs = remember(state.tabs) { state.tabs }

  val pagerState = rememberPagerState(pageCount = { tabs.size })
  val scope = rememberCoroutineScope()
  val currentPage = remember(tabs) { derivedStateOf { pagerState.targetPage } }

  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      scope.launch {
        pagerState.animateScrollToPage(0)
      }
    }
  }

  Column(modifier = Modifier.fillMaxSize()) {
    ScrollableTabRow(
      selectedTabIndex = currentPage.value,
      backgroundColor = MaterialTheme.colors.surface,
      contentColor = MaterialTheme.colors.primary,
      edgePadding = 0.dp,
    ) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          text = {
            Text(
              text = tab.name,
              fontSize = 16.sp,
              fontWeight = FontWeight.W600,
              color = if (pagerState.currentPage == index) MaterialTheme.colors.onBackground else MaterialTheme.colors.onSurface
            )
          },
          selected = true,
          onClick = {
            scope.launch {
              pagerState.animateScrollToPage(index)
            }
          }
        )
      }
    }
    HorizontalPager(
      state = pagerState,
      beyondBoundsPageCount = 1,
      key = { tabs[it].name }
    ) { index ->
      val tabValue = remember(tabs.hashCode(), index) { tabs[index].value }
      val viewModel: TabViewModel = hiltViewModel<TabViewModel>(key = "${tabs.hashCode()}, $index")

      LaunchedEffect(tabs.hashCode(), index) {
        viewModel.loadTab(tabValue)
      }

      TabScreen(
        tabId = tabValue,
        viewModel = viewModel,
        sharedViewModel = sharedViewModel,
        rootNavController = rootNavController,
        isRefreshing = state.isRefreshing,
        config = state.config
      )
    }
  }
}

@Stable
data class TabLayoutUiState(
  val tabs: List<TabDto>,
  val isRefreshing: Boolean,
  val config: Config?
)
