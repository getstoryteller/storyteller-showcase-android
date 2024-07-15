package com.getstoryteller.storytellershowcaseapp.ui.features.storyteller

import com.getstoryteller.storytellershowcaseapp.ui.features.home.VideoItemUiModel
import com.storyteller.domain.entities.Error
import com.storyteller.ui.list.StorytellerListViewDelegate
import timber.log.Timber

class PageItemStorytellerDelegate(
  private val uiModel: VideoItemUiModel,
  private val onShouldHide: (String) -> Unit = {},
  private val onPlayerDismissed: () -> Unit = {},
  private val onDataLoadComplete: () -> Unit = {},
) : StorytellerListViewDelegate {
  override fun onDataLoadComplete(
    success: Boolean,
    error: Error?,
    dataCount: Int,
  ) {
    Timber.i(
      "[$uiModel] onDataLoadComplete callback: success $success, error $error, dataCount $dataCount",
    )
    if (!success || dataCount == 0) {
      val becauseMessage = if (!success) "of error ${error?.message} " else "dataCount is 0"
      Timber.i(
        "[${uiModel.categories}] [${uiModel.collectionId}] onDataLoadComplete: calling onShouldHide, " +
          "because $becauseMessage",
      )
      onShouldHide(becauseMessage)
    }
    onDataLoadComplete()
  }

  override fun onDataLoadStarted() {
    Timber.i("[$uiModel] onDataLoadStarted callback")
  }

  override fun onPlayerDismissed() {
    Timber.i("[$uiModel] onPlayerDismissed callback")
    onPlayerDismissed.invoke()
  }
}
