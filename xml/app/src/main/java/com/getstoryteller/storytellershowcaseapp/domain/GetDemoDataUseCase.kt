package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiElement
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiPadding
import com.storyteller.domain.entities.StorytellerListViewCellType

interface GetDemoDataUseCase {
  suspend fun getDemoData(
    forceDataReload: Boolean,
    onRemoveStorytellerItem: (String) -> Unit,
  ): List<UiElement>
}

class GetDemoDataUseCaseImpl(
  private val sessionRepository: SessionRepository,
) : GetDemoDataUseCase {

  private val gridPadding
    get() = UiPadding(startPadding = 20, endPadding = 20, topPadding = 24)

  private val rowPadding
    get() = UiPadding(startPadding = 0, endPadding = 0, topPadding = 24)

  override suspend fun getDemoData(
    forceDataReload: Boolean,
    onRemoveStorytellerItem: (String) -> Unit,
  ): List<UiElement> {
    val categories = sessionRepository.categories
    val collection = sessionRepository.collection
    return listOf(
      UiElement.StoryRow(
        title = categories.title,
        cellType = StorytellerListViewCellType.ROUND,
        height = 116,
        categories = categories.categories,
        forceDataReload = forceDataReload,
        padding = rowPadding.copy(topPadding = 8),
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.StoryRow(
        title = categories.title,
        cellType = StorytellerListViewCellType.SQUARE,
        height = 174,
        categories = categories.categories,
        forceDataReload = forceDataReload,
        padding = rowPadding,
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.StoryGrid(
        title = categories.title,
        cellType = StorytellerListViewCellType.SQUARE,
        categories = categories.categories,
        forceDataReload = forceDataReload,
        padding = gridPadding,
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.ClipRow(
        title = collection.title,
        cellType = StorytellerListViewCellType.SQUARE,
        height = 174,
        collection = collection.collection,
        forceDataReload = forceDataReload,
        padding = rowPadding,
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
      UiElement.ClipGrid(
        title = collection.title,
        cellType = StorytellerListViewCellType.SQUARE,
        collection = collection.collection,
        forceDataReload = forceDataReload,
        padding = gridPadding.copy(bottomPadding = 8),
        onFailure = { idToRemove -> onRemoveStorytellerItem(idToRemove) },
      ),
    )
  }
}
