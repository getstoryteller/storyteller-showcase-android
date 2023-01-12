package com.example.storytellerSampleAndroid.multiple.adapter

import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import com.storyteller.domain.StorytellerListViewCellType
import com.storyteller.domain.theme.builders.UiTheme
import java.util.*

data class UiPadding(
    @Dimension(unit = Dimension.DP) val startPadding: Int = 0,
    @Dimension(unit = Dimension.DP) val endPadding: Int = 0,
    @Dimension(unit = Dimension.DP) val topPadding: Int = 0,
    @Dimension(unit = Dimension.DP) val bottomPadding: Int = 0
) {
    companion object {
        val NONE = UiPadding(0, 0, 0, 0)
    }
}

sealed class UiElement(
    val id: String,
    open val padding: UiPadding
) {

    data class StoryRow(
        val cellType: StorytellerListViewCellType,
        @Dimension(unit = Dimension.DP) val height: Int,
        val theme: UiTheme? = null,
        val categories: List<String>,
        var forceDataReload: Boolean,
        val onFailure: (String) -> Unit,
        override val padding: UiPadding = UiPadding.NONE
    ) : UiElement(categories.toString(), padding)

    data class StoryGrid(
        val cellType: StorytellerListViewCellType,
        val theme: UiTheme? = null,
        val categories: List<String>,
        var forceDataReload: Boolean,
        val onFailure: (String) -> Unit,
        override val padding: UiPadding = UiPadding.NONE
    ) : UiElement(categories.toString(), padding)

    data class ClipRow(
        val cellType: StorytellerListViewCellType,
        @Dimension(unit = Dimension.DP) val height: Int,
        val theme: UiTheme? = null,
        val collection: String,
        var forceDataReload: Boolean,
        val onFailure: (String) -> Unit,
        override val padding: UiPadding = UiPadding.NONE
    ) : UiElement(collection, padding)

    data class ClipGrid(
        val cellType: StorytellerListViewCellType,
        val theme: UiTheme? = null,
        val collection: String,
        var forceDataReload: Boolean,
        val onFailure: (String) -> Unit,
        override val padding: UiPadding = UiPadding.NONE
    ) : UiElement(collection, padding)

}
