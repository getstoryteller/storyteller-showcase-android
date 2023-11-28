package com.getstoryteller.storytellersampleapp.ui

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun StorytellerClipsFragmentComponent(
    fragmentManager: FragmentManager,
    modifier: Modifier = Modifier,
    collectionId: String
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val containerId = ViewCompat.generateViewId()
            val fragmentContainerView = FragmentContainerView(context).apply {
                id = containerId
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val fragment = StorytellerClipsFragment.create(collectionId)
            fragmentManager.beginTransaction()
                .replace(containerId, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()

            fragmentContainerView
        }
    )
}
