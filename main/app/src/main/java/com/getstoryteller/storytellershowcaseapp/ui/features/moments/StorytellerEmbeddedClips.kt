package com.getstoryteller.storytellershowcaseapp.ui.features.moments

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun StorytellerEmbeddedClips(
  modifier: Modifier = Modifier,
  onCommit: FragmentTransaction.(containerId: Int) -> Unit,
  onSaveInstanceState: FragmentTransaction.(fragment: Fragment) -> Unit,
) {
  val localView = LocalView.current
  val parentFragment =
    remember(localView) {
      try {
        localView.findFragment<Fragment>()
      } catch (e: IllegalStateException) {
        // findFragment throws if no parent fragment is found
        null
      }
    }

  val fmgr = parentFragment?.childFragmentManager ?: (localView.context as? FragmentActivity)?.supportFragmentManager
  val containerId by rememberSaveable { mutableIntStateOf(View.generateViewId()) }
  val container = remember { mutableStateOf<FragmentContainerView?>(null) }
  val viewBlock: (Context) -> View =
    remember(localView) {
      { context ->
        FragmentContainerView(context).apply { id = containerId }.also {
          fmgr?.commit { onCommit(it.id) }
          container.value = it
        }
      }
    }
  AndroidView(
    modifier = modifier,
    factory = viewBlock,
    update = {},
  )

  DisposableEffect(localView, container) {
    onDispose {
      val existingFragment = fmgr?.findFragmentById(container.value?.id ?: 0) as? StorytellerClipsFragment
      if (existingFragment != null && !fmgr.isStateSaved) {
        fmgr.commit { onSaveInstanceState(existingFragment) }
      }
    }
  }
}
