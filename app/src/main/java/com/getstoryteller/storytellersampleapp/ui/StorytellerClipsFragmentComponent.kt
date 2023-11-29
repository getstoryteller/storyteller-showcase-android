package com.getstoryteller.storytellersampleapp.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun StorytellerClipsFragmentComponent(
  fragmentManager: FragmentManager,
  activity: Activity,
  modifier: Modifier = Modifier,
  collectionId: String,
  shouldPlay: MutableState<Boolean>
) {
  val containerId = remember { View.generateViewId() }
  val fragment = remember {
    StorytellerClipsFragment.create(collectionId)
  }



  DisposableEffect(fragment) {
    Log.d("FINA", "StorytellerClipsFaaaaaragmentComponent: aaaa")
    if (fragmentManager.findFragmentByTag("StorytellerClipsFragment") == null) {
      fragmentManager.beginTransaction().apply {
        addToBackStack(null)
        replace(containerId, fragment, "StorytellerClipsFragment")
        commit()
      }
    }

    onDispose {
//      if (!fragmentManager.isStateSaved) {
//        fragmentManager.beginTransaction().apply {
//          remove(fragment)
//          commit()
//        }
//      }
    }
  }

  AndroidView(
    modifier = modifier,
    factory = { context ->
      FragmentContainerView(context = context).apply {
        id = containerId
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
      }
    },
    update = {
      val fragment =
        fragmentManager.findFragmentByTag("StorytellerClipsFragment") as? StorytellerClipsFragment
      Log.d("FINA", "StorytellerClipsFragmentComponent: $fragment")
      // fragment?.shouldPlay = shouldPlay.value
    }
  )
}
