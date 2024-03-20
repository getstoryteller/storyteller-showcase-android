package com.getstoryteller.storytellershowcaseapp.ui.features.moments

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment
import com.getstoryteller.storytellershowcaseapp.ui.features.MainActivity.Companion.SAVED_STATE_FRAGMENT_ID

@Composable
fun StorytellerEmbeddedClips(
  modifier: Modifier = Modifier,
  onCommit: FragmentTransaction.() -> Unit,
  onSaveState: (Fragment) -> Unit,
) {
  val localView = LocalView.current
  val parentFragment = remember(localView) {
    try {
      localView.findFragment<Fragment>()
    } catch (e: IllegalStateException) {
      // findFragment throws if no parent fragment is found
      null
    }
  }

  val container = remember { mutableStateOf<FragmentContainerView?>(null) }
  val viewBlock: (Context) -> View = remember(localView) {
    { context ->
      FragmentContainerView(context)
        .apply { id = SAVED_STATE_FRAGMENT_ID }
        .also {
          val fragmentManager = parentFragment?.childFragmentManager
            ?: (context as? FragmentActivity)?.supportFragmentManager
          fragmentManager?.commit { onCommit() }
          container.value = it
        }
    }
  }
  AndroidView(
    modifier = modifier,
    factory = viewBlock,
    update = {},
  )

  // Set up a DisposableEffect that will clean up fragments when the FragmentContainer is disposed
  val localContext = LocalContext.current
  DisposableEffect(localView, localContext, container) {
    onDispose {
      val fragmentManager = parentFragment?.childFragmentManager
        ?: (localContext as? FragmentActivity)?.supportFragmentManager
      val existingFragment = fragmentManager?.findFragmentById(container.value?.id ?: 0)
      if (existingFragment != null && !fragmentManager.isStateSaved) {
        // If the state isn't saved, that means that some state change
        // has removed this Composable from the hierarchy
        fragmentManager.commit {
          remove(existingFragment)
          onSaveState(existingFragment)
        }
      }
    }
  }
}
