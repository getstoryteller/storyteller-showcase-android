package com.getstoryteller.storytellershowcaseapp.ui.features.moments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.storyteller.domain.entities.Error
import com.storyteller.ui.pager.StorytellerClipsFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// This view embeds the StorytellerClipsFragment in a tab of its own.
// There is more information available about this in our public documentation
// https://www.getstoryteller.com/documentation/android/storyteller-clips-fragment

@Composable
fun MomentsScreen(
  modifier: Modifier = Modifier,
  clipsFragment: StorytellerClipsFragment,
  sharedViewModel: MainViewModel,
  onCommit: (fragment: Fragment, tag: String) -> FragmentTransaction.(containerId: Int) -> Unit,
  onSaveInstanceState: FragmentTransaction.(fragment: Fragment) -> Unit,
  onSetTopBarVisible: (Boolean) -> Unit,
  onMomentsTabLoading: (Boolean) -> Unit,
) {
  val reloadDataTrigger by sharedViewModel.reloadMomentsDataTrigger.collectAsState(null)

  LaunchedEffect(reloadDataTrigger) {
    if (reloadDataTrigger == null) return@LaunchedEffect
    clipsFragment.goBack()
  }

  LaunchedEffect(Unit) {
    onSetTopBarVisible(false)
  }

  val momentsReloadTimeout by sharedViewModel.momentsReloadTimeout.collectAsState()
  LaunchedEffect(momentsReloadTimeout) {
    if (momentsReloadTimeout) {
      sharedViewModel.momentsDisposed()
      sharedViewModel.triggerMomentsReloadData()
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      sharedViewModel.momentsDisposed()
    }
  }

  Box(
    modifier = modifier.fillMaxSize(),
  ) {
    var isProgressVisible by rememberSaveable(clipsFragment) { mutableStateOf(false) }
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(clipsFragment) {
      clipsFragment.listener =
        object : StorytellerClipsFragment.Listener {
          override fun onDataLoadStarted() {
            isProgressVisible = true
            onMomentsTabLoading(true)
          }

          override fun onDataLoadComplete(
            success: Boolean,
            error: Error?,
            dataCount: Int,
          ) {
            coroutineScope.launch {
              isProgressVisible = false
              delay(500)
              onMomentsTabLoading(false)
            }
          }
        }

      ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
        val outInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        clipsFragment.topInset = outInsets.top
        WindowInsetsCompat.CONSUMED
      }
    }

    StorytellerEmbeddedClips(
      modifier = Modifier.fillMaxSize(),
      onCommit = onCommit(clipsFragment, "moments-fragment"),
      onSaveInstanceState = onSaveInstanceState,
    )

    if (isProgressVisible) {
      CircularProgressIndicator(
        modifier =
        Modifier
          .padding(16.dp)
          .background(color = Color.Transparent)
          .align(Alignment.Center),
      )
    }
  }
}
