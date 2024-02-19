package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.ShowcaseApp.Companion.CLIP_COLLECTION
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiElement
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiPadding
import com.storyteller.domain.entities.StorytellerListViewCellType

interface GetDemoDataUseCase {
  fun getDemoData(
    forceDataReload: Boolean,
    onRemoveStorytellerItem: (String) -> Unit,
  ): List<UiElement>
}

class GetDemoDataUseCaseImpl : GetDemoDataUseCase {

  private val gridPadding
    get() = UiPadding(startPadding = 20, endPadding = 20, topPadding = 24)

  private val rowPadding
    get() = UiPadding(startPadding = 0, endPadding = 0, topPadding = 24)

  override fun getDemoData(
    forceDataReload: Boolean,
    onRemoveStorytellerItem: (String) -> Unit,
  ): List<UiElement> =
    listOf(
      UiElement.StoryRow(
        cellType = StorytellerListViewCellType.ROUND,
        height = 116,
        categories = listOf(""),
        forceDataReload = forceDataReload,
        padding = rowPadding.copy(topPadding = 8),
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.StoryRow(
        cellType = StorytellerListViewCellType.SQUARE,
        height = 174,
        categories = listOf(""),
        forceDataReload = forceDataReload,
        padding = rowPadding,
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.StoryGrid(
        cellType = StorytellerListViewCellType.SQUARE,
        categories = listOf(""),
        forceDataReload = forceDataReload,
        padding = gridPadding,
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.ClipRow(
        cellType = StorytellerListViewCellType.SQUARE,
        height = 174,
        collection = CLIP_COLLECTION,
        forceDataReload = forceDataReload,
        padding = rowPadding,
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.ClipGrid(
        cellType = StorytellerListViewCellType.SQUARE,
        collection = CLIP_COLLECTION,
        forceDataReload = forceDataReload,
        padding = gridPadding.copy(bottomPadding = 8),
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
    )
}
