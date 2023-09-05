package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storytellerSampleAndroid.JetpackComposeActivity
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel
import com.storyteller.ui.compose.StorytellerComposeController

@Composable
fun MainEntrypoint(
  controller: StorytellerComposeController,
  viewModel: JetpackComposeViewModel,
  onRefresh: () -> Unit,
  storytellerListViewDelegate: JetpackComposeActivity,
  onUserNavigatedToApp: (String) -> Unit
) {
  Scaffold(topBar = { TopBar() }) {
    MainScreen(
      controller = controller,
      viewModel = viewModel,
      onRefresh = onRefresh,
      onUserNavigatedToApp = onUserNavigatedToApp,
      storytellerListViewDelegate = storytellerListViewDelegate,
      paddingValues = it,
    )
  }
}
