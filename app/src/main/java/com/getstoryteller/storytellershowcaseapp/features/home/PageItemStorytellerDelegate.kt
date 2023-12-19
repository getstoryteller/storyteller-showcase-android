package com.getstoryteller.storytellershowcaseapp.features.home

import com.storyteller.domain.entities.Error
import com.storyteller.ui.list.StorytellerListViewDelegate
import timber.log.Timber

class PageItemStorytellerDelegate(
  private val itemId: String,
  private val onDataLoadComplete: (itemId: String) -> Unit = { _ -> },
) : StorytellerListViewDelegate {
  override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
    Timber.i(
      "onDataLoadComplete callback for item $itemId: success $success, error $error, dataCount $dataCount"
    )
    onDataLoadComplete(itemId)
  }

  override fun onDataLoadStarted() {
    Timber.i("onDataLoadStarted callback")
  }

  override fun onPlayerDismissed() {
    Timber.i("onPlayerDismissed callback")
  }
}
