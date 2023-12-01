package com.getstoryteller.storytellersampleapp.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.TabDto
import com.getstoryteller.storytellersampleapp.domain.Config
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TabLayout(
  tabs: List<TabDto>,
  rootNavController: NavController,
  isRefreshing: Boolean,
  config: Config?
) {
  val titles = tabs.map { it.name }
  val pagerState = rememberPagerState(pageCount = tabs.size)
  val scope = rememberCoroutineScope()

  Column(modifier = Modifier.fillMaxSize()) {
    ScrollableTabRow(
      selectedTabIndex = pagerState.currentPage,
      backgroundColor = MaterialTheme.colors.surface,
      contentColor = MaterialTheme.colors.primary,
      edgePadding = 0.dp,
    ) {
      titles.forEachIndexed { index, title ->
        Tab(
          text = {
            Text(
              text = title,
              color = if (pagerState.currentPage == index) MaterialTheme.colors.onBackground else MaterialTheme.colors.onSurface
            )
          },
          selected = pagerState.currentPage == index,
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
      dragEnabled = false
    ) { index ->
      val tabValue = tabs[index].value
      val viewModel: TabViewModel =
        hiltViewModel<TabViewModel>(key = tabValue).apply { loadTab(tabValue) }
      TabScreen(
        tabId = tabValue,
        viewModel = viewModel,
        rootNavController = rootNavController,
        isRefreshing = isRefreshing,
        config = config
      )
    }
  }
}
