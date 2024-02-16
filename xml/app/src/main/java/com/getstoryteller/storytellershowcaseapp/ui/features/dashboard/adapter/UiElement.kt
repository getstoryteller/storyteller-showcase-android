package com.getstoryteller.storytellershowcaseapp.ui.features.mainxml.adapter

import androidx.annotation.Dimension
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.theme.builders.UiTheme

data class UiPadding(
  @Dimension(unit = Dimension.DP) val startPadding: Int = 0,
  @Dimension(unit = Dimension.DP) val endPadding: Int = 0,
  @Dimension(unit = Dimension.DP) val topPadding: Int = 0,
  @Dimension(unit = Dimension.DP) val bottomPadding: Int = 0,
) {
  companion object {
    val NONE = UiPadding(0, 0, 0, 0)
  }
}

sealed class UiElement(
  val id: String,
  open val padding: UiPadding,
) {

  data class StoryRow(
    val cellType: StorytellerListViewCellType,
    @Dimension(unit = Dimension.DP) val height: Int,
    val theme: UiTheme? = null,
    val categories: List<String>,
    var forceDataReload: Boolean,
    val onFailure: (String) -> Unit,
    override val padding: UiPadding = UiPadding.NONE,
  ) : UiElement(categories.toString(), padding)

  data class StoryGrid(
    val cellType: StorytellerListViewCellType,
    val theme: UiTheme? = null,
    val categories: List<String>,
    var forceDataReload: Boolean,
    val onFailure: (String) -> Unit,
    val displayLimit: Int = 2,
    override val padding: UiPadding = UiPadding.NONE,
  ) : UiElement(categories.toString(), padding)

  data class ClipRow(
    val cellType: StorytellerListViewCellType,
    @Dimension(unit = Dimension.DP) val height: Int,
    val theme: UiTheme? = null,
    val collection: String,
    var forceDataReload: Boolean,
    val onFailure: (String) -> Unit,
    override val padding: UiPadding = UiPadding.NONE,
  ) : UiElement(collection, padding)

  data class ClipGrid(
    val cellType: StorytellerListViewCellType,
    val theme: UiTheme? = null,
    val collection: String,
    var forceDataReload: Boolean,
    val onFailure: (String) -> Unit,
    val displayLimit: Int = 4,
    override val padding: UiPadding = UiPadding.NONE,
  ) : UiElement(collection, padding)
}
