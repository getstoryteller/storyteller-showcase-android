package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter

import androidx.annotation.Dimension
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.theme.builders.UiTheme

sealed class UiElement(
  val id: String,
) {

  var onFailure: ((String) -> Unit)? = null

  data class StoryRow(
    val title: String,
    val more: String,
    val cellType: StorytellerListViewCellType,
    @Dimension(unit = Dimension.DP) val height: Int,
    val theme: UiTheme? = null,
    val categories: List<String>,
    var forceDataReload: Boolean,
  ) : UiElement(categories.toString())

  data class StoryGrid(
    val title: String,
    val more: String,
    val cellType: StorytellerListViewCellType,
    val theme: UiTheme? = null,
    val categories: List<String>,
    var forceDataReload: Boolean,
    val displayLimit: Int = 2,
  ) : UiElement(categories.toString())

  data class ClipRow(
    val title: String,
    val more: String,
    val cellType: StorytellerListViewCellType,
    @Dimension(unit = Dimension.DP) val height: Int,
    val theme: UiTheme? = null,
    val collection: String,
    var forceDataReload: Boolean,
  ) : UiElement(collection)

  data class ClipGrid(
    val title: String,
    val more: String,
    val cellType: StorytellerListViewCellType,
    val theme: UiTheme? = null,
    val collection: String,
    var forceDataReload: Boolean,
    val displayLimit: Int = 4,
  ) : UiElement(collection)

  data class ClipSingleton(
    val title: String,
    val more: String,
    val cellType: StorytellerListViewCellType,
    val theme: UiTheme? = null,
    val collection: String,
    var forceDataReload: Boolean,
  ) : UiElement(collection)

  data class StorySingleton(
    val title: String,
    val more: String,
    val cellType: StorytellerListViewCellType,
    val theme: UiTheme? = null,
    val categories: List<String>,
    var forceDataReload: Boolean,
  ) : UiElement(categories.toString())
}
