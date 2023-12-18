package com.getstoryteller.storytellersampleapp.features.moments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.features.main.MainViewModel
import com.getstoryteller.storytellersampleapp.ui.StorytellerEmbeddedClips
import com.storyteller.domain.entities.Error
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun MomentsScreen(
  modifier: Modifier,
  config: Config?,
  sharedViewModel: MainViewModel,
  onCommit: (fragment: Fragment, tag: String) -> FragmentTransaction.(containerId: Int) -> Unit,
  getClipsFragment: () -> StorytellerClipsFragment?,
  tag: String
) {

  val reloadDataTrigger by sharedViewModel.reloadMomentsDataTrigger.observeAsState()
  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      getClipsFragment()?.reloadData()
    }
  }

  Box(
    modifier = modifier.fillMaxSize()
  ) {
    var isVisible by remember(true) { mutableStateOf(true) }

    val clipsFragment by remember(config) {
      mutableStateOf(
        StorytellerClipsFragment.create(config?.topLevelCollectionId ?: "", topLevelBackEnabled = true)
      )
    }

    LaunchedEffect(clipsFragment) {
      clipsFragment.listener = object : StorytellerClipsFragment.Listener {
        override fun onDataLoadStarted() = Unit

        override fun onTopLevelBackPressed(): Boolean {
          return super.onTopLevelBackPressed()
        }

        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          isVisible = false
        }
      }
    }

    StorytellerEmbeddedClips(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
      onCommit = onCommit(
        clipsFragment,
        tag,
      )
    )

    AnimatedVisibility(modifier = Modifier.align(Alignment.Center), visible = isVisible) {
      CircularProgressIndicator(
        modifier = Modifier
          .padding(16.dp)
          .background(color = Color.Transparent)
      )
    }
  }
}
