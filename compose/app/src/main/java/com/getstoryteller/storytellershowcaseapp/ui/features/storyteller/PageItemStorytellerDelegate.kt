package com.getstoryteller.storytellershowcaseapp.ui.features.storyteller

import com.storyteller.domain.entities.Error
import com.storyteller.ui.list.StorytellerListViewDelegate
import timber.log.Timber

class PageItemStorytellerDelegate(
  private val itemId: String,
  private val onShouldHide: () -> Unit = {},
  private val onPlayerDismissed: () -> Unit = {},
) : StorytellerListViewDelegate {
  override fun onDataLoadComplete(
    success: Boolean,
    error: Error?,
    dataCount: Int,
  ) {
    Timber.i(
      "[$itemId] onDataLoadComplete callback: success $success, error $error, dataCount $dataCount",
    )
    if (!success || dataCount == 0) {
      val becauseMessage = if (!success) "of error" else "dataCount is 0"
      Timber.i("[$itemId] onDataLoadComplete: calling onShouldHide because $becauseMessage")
      onShouldHide()
    }
  }

  override fun onDataLoadStarted() {
    Timber.i("[$itemId] onDataLoadStarted callback")
  }

  override fun onPlayerDismissed() {
    Timber.i("[$itemId] onPlayerDismissed callback")
    onPlayerDismissed.invoke()
  }
}
