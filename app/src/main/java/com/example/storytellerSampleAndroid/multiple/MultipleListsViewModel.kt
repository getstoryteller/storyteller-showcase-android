package com.example.storytellerSampleAndroid.multiple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storytellerSampleAndroid.multiple.adapter.UiElement
import com.example.storytellerSampleAndroid.multiple.adapter.UiPadding
import com.storyteller.domain.StorytellerListViewCellType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MultipleListsViewModel : ViewModel() {

  private val _uiData = MutableStateFlow<List<UiElement>>(listOf())
  val uiData = _uiData.asSharedFlow()

  private val gridPadding
    get() = UiPadding(startPadding = 20, endPadding = 20, topPadding = 24)

  private val rowPadding
    get() = UiPadding(startPadding = 0, endPadding = 0, topPadding = 24)

  init {
    reload()
  }

  fun reload(forceDataReload: Boolean = true) {
    viewModelScope.launch {
      _uiData.emit(getDemoData(forceDataReload))
    }
  }

  private fun getDemoData(
    forceDataReload: Boolean
  ): List<UiElement> = listOf(
    UiElement.StoryRow(
      cellType = StorytellerListViewCellType.ROUND,
      height = 116,
      categories = listOf(""),
      forceDataReload = forceDataReload,
      padding = rowPadding.copy(topPadding = 8),
      onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) }
    ),
    UiElement.StoryRow(
      cellType = StorytellerListViewCellType.SQUARE,
      height = 174,
      categories = listOf(""),
      forceDataReload = forceDataReload,
      padding = rowPadding,
      onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) }
    ),
    UiElement.StoryGrid(
      cellType = StorytellerListViewCellType.SQUARE,
      categories = listOf(""),
      forceDataReload = forceDataReload,
      padding = gridPadding,
      onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) }
    ),
    UiElement.ClipRow(
      cellType = StorytellerListViewCellType.SQUARE,
      height = 174,
      collection = "clipssample",
      forceDataReload = forceDataReload,
      padding = rowPadding,
      onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) }
    ),
    UiElement.ClipGrid(
      cellType = StorytellerListViewCellType.SQUARE,
      collection = "clipssample",
      forceDataReload = forceDataReload,
      padding = gridPadding.copy(bottomPadding = 8),
      onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) }
    )
  )

  private fun onRemoveStorytellerItem(idToRemove: String) {
    val uiData = _uiData.value.filter { it.id != idToRemove }
    viewModelScope.launch {
      _uiData.emit(uiData)
    }
  }
}