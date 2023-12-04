package com.getstoryteller.storytellersampleapp.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TabLayout(
  tabs: List<TabDto>,
  rootNavController: NavController,
  isRefreshing: Boolean,
  config: Config?
) {
  val titles = tabs.map { it.name }
  val pagerState = rememberPagerState(pageCount = { tabs.size })
  val scope = rememberCoroutineScope()
  var currentPage = remember { derivedStateOf { pagerState.targetPage } }

//  val fling = PagerDefaults.flingBehavior(
//    state = pagerState,
//    pagerSnapDistance = PagerSnapDistance.atMost(1)
//  )

  Column(modifier = Modifier.fillMaxSize()) {
    ScrollableTabRow(
      selectedTabIndex = currentPage.value,
      backgroundColor = MaterialTheme.colors.surface,
      contentColor = MaterialTheme.colors.primary,
      edgePadding = 0.dp,
    ) {
      titles.forEachIndexed { index, title ->
        Tab(
          text = {
            Text(
              text = title,
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
      beyondBoundsPageCount = 10
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
