package com.example.storytellerSampleAndroid.multiple

import android.util.Log
import com.storyteller.services.Error
import com.storyteller.ui.list.StorytellerListViewDelegate

class StorytellerViewDelegate(private val rowId: String, val onDataFailed: (String) -> Unit) :
  StorytellerListViewDelegate {

  /*
  Called when the data loading network request is complete
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#ErrorHandling
  */
  override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
    Log.i(
      "Storyteller Sample",
      "onDataLoadComplete callback: success $success, error $error, dataCount $dataCount"
    )
    // if there is no data remove item from recycler
    if(!success || dataCount < 1) onDataFailed(rowId)
  }

  /*
  Called when the network request to load data for all stories has started
   */
  override fun onDataLoadStarted() {
    Log.i("Storyteller Sample", "onDataLoadStarted callback")
  }

  /*
  Called when story player has been dismissed
   */
  override fun onPlayerDismissed() {
    Log.i("Storyteller Sample", "onPlayerDismissed callback")
  }

  /*
  Called whenever a tile is visible in the story view
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#TileVisibility
   */
  override fun tileBecameVisible(contentIndex: Int) {
    Log.i("Storyteller Sample", "tileBecameVisible: storyIndex $contentIndex")
  }
}