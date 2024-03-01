package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter

import com.storyteller.ui.list.StorytellerListViewDelegate
import timber.log.Timber

class StorytellerViewDelegate(private val rowId: String, val onDataFailed: (String) -> Unit) :
  StorytellerListViewDelegate {

  /*
  Called when the data loading network request is complete
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#ErrorHandling
   */
  override fun onDataLoadComplete(
    success: Boolean,
    error: com.storyteller.domain.entities.Error?,
    dataCount: Int,
  ) {
    Timber
      .tag("Storyteller Sample")
      .i("onDataLoadComplete callback: success $success, error $error, dataCount $dataCount")
    // if there is no data remove item from recycler
    if (!success || dataCount < 1) onDataFailed(rowId)
  }

  /*
  Called when the network request to load data for all stories has started
   */
  override fun onDataLoadStarted() {
    Timber
      .tag("Storyteller Sample")
      .i("onDataLoadStarted callback")
  }

  /*
  Called when story player has been dismissed
   */
  override fun onPlayerDismissed() {
    Timber
      .tag("Storyteller Sample")
      .i("onPlayerDismissed callback")
  }
}
