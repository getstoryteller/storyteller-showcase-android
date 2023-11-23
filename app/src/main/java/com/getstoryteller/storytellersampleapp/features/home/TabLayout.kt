package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getstoryteller.storytellersampleapp.data.TabDto

@Composable
fun TabLayout(
    tabs: List<TabDto>,
    rootNavController: NavController
) {
    val titles = tabs.map { it.name }
    var tabIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tabIndex) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                            navController.navigate(tabs[index].value)
                        }
                    )
                }
            }
        },
        content = { paddingValues ->
            NavHost(
                navController = navController, startDestination = "home",
                modifier = Modifier
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                tabs.forEach { tab ->
                    composable(tab.value) {
                        TabScreen(
                            tabId = tab.value,
                            viewModel = hiltViewModel(),
                            rootNavController = rootNavController
                        )
                    }
                }
            }
        }
    )
}