package com.getstoryteller.storytellershowcaseapp.ui.features.moments

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.getstoryteller.storytellershowcaseapp.ui.features.main.KeepScreenOn
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.storyteller.domain.entities.Error
import com.storyteller.ui.compose.embedded.StorytellerEmbeddedClips
import com.storyteller.ui.compose.embedded.rememberStorytellerEmbeddedClipsState
import com.storyteller.ui.pager.StorytellerClipsFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// This view embeds the StorytellerClipsFragment in a tab of its own.
// There is more information available about this in our public documentation
// https://www.getstoryteller.com/documentation/android/storyteller-clips-fragment

@Composable
fun MomentsScreen(
  modifier: Modifier = Modifier,
  collection: String,
  startClip: String?,
  sharedViewModel: MainViewModel,
  viewModel: MomentsViewModel,
  onSetTopBarVisible: (Boolean) -> Unit,
  onMomentsTabLoading: (Boolean) -> Unit,
  onLocationChanged: (String) -> Unit,
) {
  KeepScreenOn()
  val reloadDataTrigger by sharedViewModel.reloadMomentsDataTrigger.collectAsState(null)
  val uiState by sharedViewModel.uiState.collectAsState()

  LaunchedEffect(Unit) {
    onSetTopBarVisible(false)
    onLocationChanged("Moments")
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

  val lifecycleOwner = LocalLifecycleOwner.current
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_STOP) {
        viewModel.scheduleMomentsRefresh()
      }
      if (event == Lifecycle.Event.ON_RESUME) {
        if (viewModel.shouldReloadMomentsData()) {
          sharedViewModel.triggerMomentsReloadData()
        }
      }
    }

    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  Box(
    modifier = modifier.fillMaxSize(),
  ) {
    var isProgressVisible by rememberSaveable(collection) { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listener = remember {
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
    }

    val embeddedClipsState = rememberStorytellerEmbeddedClipsState(
      collectionId = collection,
      clipId = startClip,
      topLevelBack = false,
    )

    LaunchedEffect(Unit) {
      embeddedClipsState.setListener(listener)
    }

    LaunchedEffect(reloadDataTrigger) {
      if (reloadDataTrigger == null) return@LaunchedEffect
      embeddedClipsState.reloadData()
    }

    StorytellerEmbeddedClips(
      modifier = Modifier,
      state = embeddedClipsState,
    )

    if (isProgressVisible) {
      val isDarkTheme = isSystemInDarkTheme()
      val progressColor = if (isDarkTheme) {
        uiState.config?.squareTheme?.dark
      } else {
        uiState.config?.squareTheme?.light
      }?.colors?.primary?.let {
        Color(it)
      } ?: MaterialTheme.colorScheme.primary

      CircularProgressIndicator(
        modifier = Modifier
          .padding(16.dp)
          .background(color = Color.Transparent)
          .align(Alignment.Center),
        color = progressColor,
      )
    }
  }
}
