package com.getstoryteller.storytellersampleapp.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun FragmentContainer(
  modifier: Modifier = Modifier,
  onCommit: FragmentTransaction.(containerId: Int) -> Unit,
) {
  val localContext = LocalContext.current
  val fmgr = (localContext as? FragmentActivity)?.supportFragmentManager
  val containerId by rememberSaveable { mutableIntStateOf(View.generateViewId()) }
  val container = remember { mutableStateOf<FragmentContainerView?>(null) }
  val viewBlock: (Context) -> View = remember(localContext) {
    { context ->
      FragmentContainerView(context)
        .apply {
          id = containerId
        }
        .also {
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

  // Set up a DisposableEffect that will clean up fragments when the FragmentContainer is disposed
  DisposableEffect(localContext, container) {
    onDispose {
      val existingFragment = fmgr?.findFragmentById(container.value?.id ?: 0) as? StorytellerClipsFragment
      existingFragment?.shouldPlay = false
    }
  }
}
