package com.getstoryteller.storytellershowcaseapp.ui.features.moments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.storyteller.domain.entities.Error
import com.storyteller.ui.pager.StorytellerClipsFragment

// This view embeds the StorytellerClipsFragment in a tab of its own.
// There is more information available about this in our public documentation
// https://www.getstoryteller.com/documentation/android/storyteller-clips-fragment

@Composable
fun MomentsScreen(
  modifier: Modifier = Modifier,
  config: Config?,
  sharedViewModel: MainViewModel,
  onCommit: (fragment: Fragment, tag: String) -> FragmentTransaction.(containerId: Int) -> Unit,
  getClipsFragment: () -> StorytellerClipsFragment?,
  tag: String,
) {
  val reloadDataTrigger by sharedViewModel.reloadMomentsDataTrigger.observeAsState()
  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      getClipsFragment()?.reloadData()
    }
  }

  Box(
    modifier = modifier.fillMaxSize(),
  ) {
    val clipsFragment by remember(config) {
      mutableStateOf(
        StorytellerClipsFragment.create(config?.topLevelCollectionId ?: ""),
      )
    }

    var isVisible by rememberSaveable(clipsFragment) { mutableStateOf(false) }
    val view = LocalView.current
    LaunchedEffect(clipsFragment) {
      clipsFragment.listener =
        object : StorytellerClipsFragment.Listener {
          override fun onDataLoadStarted() {
            isVisible = true
          }

          override fun onTopLevelBackPressed(): Boolean {
            return super.onTopLevelBackPressed()
          }

          override fun onDataLoadComplete(
            success: Boolean,
            error: Error?,
            dataCount: Int,
          ) {
            isVisible = false
          }
        }

      ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
        val outInsets =
          insets.getInsets(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        clipsFragment.topInset = outInsets.top
        WindowInsetsCompat.CONSUMED
      }
    }

    StorytellerEmbeddedClips(
      modifier =
        Modifier
          .fillMaxWidth()
          .fillMaxHeight(),
      onCommit =
        onCommit(
          clipsFragment,
          tag,
        ),
    )

    if (isVisible) {
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
