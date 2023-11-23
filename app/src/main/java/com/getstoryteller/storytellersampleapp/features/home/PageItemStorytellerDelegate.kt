package com.getstoryteller.storytellersampleapp.features.home

import com.storyteller.domain.entities.Error
import com.storyteller.ui.list.StorytellerListViewDelegate
import timber.log.Timber

class PageItemStorytellerDelegate(private val itemId: String, val onDataLoadFailed: (String) -> Unit) :
    StorytellerListViewDelegate {
    override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Timber.i(
            "onDataLoadComplete callback for item $itemId: success $success, error $error, dataCount $dataCount"
        )
        if (!success || dataCount < 1) {
            onDataLoadFailed(itemId)
        }
    }

    override fun onDataLoadStarted() {
        Timber.i("onDataLoadStarted callback")
    }

    override fun onPlayerDismissed() {
        Timber.i("onPlayerDismissed callback")
    }
}
