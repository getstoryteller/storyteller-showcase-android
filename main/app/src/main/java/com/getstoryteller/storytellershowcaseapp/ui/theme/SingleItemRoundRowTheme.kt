package com.getstoryteller.storytellershowcaseapp.ui.theme

import com.getstoryteller.storytellershowcaseapp.R
import com.storyteller.domain.entities.theme.builders.ThemeBuilder.StorytellerResource.StorytellerDrawable.Companion.drawableRes
import com.storyteller.domain.entities.theme.builders.buildTheme
import com.storyteller.domain.entities.theme.builders.from
import com.storyteller.domain.entities.theme.builders.ofHexCode

object SingleItemRoundRowTheme {

  val theme = buildTheme {

    light {
      lists {
        row {
          startInset = 0
          endInset = 0
          tileSpacing = 0
        }
      }

      tiles {
        circularTile {
          readBorderWidth = 2
          unreadBorderWidth = 2
          unreadIndicatorColor = ofHexCode("#F9BF4B")
          readIndicatorColor = ofHexCode("#F9BF4B")
          liveChip {
            readBackgroundColor = ofHexCode("#F9BF4B")
            unreadBackgroundColor = ofHexCode("#F9BF4B")
            unreadImage = drawableRes(R.drawable.transparent_drawable)
            readImage = drawableRes(R.drawable.transparent_drawable)
          }
        }
        title {
          show = false
        }
      }
    }

    dark from light
  }

  /**
   *   StorytellerStoriesRow(
   * +          modifier = Modifier.size(40.dp, 40.dp),
   * +          dataModel = StorytellerStoriesDataModel(
   * +            categories = emptyList(),
   * +            displayLimit = 1,
   * +            theme = SingleItemRoundRowTheme.theme,
   * +            cellType = StorytellerListViewCellType.ROUND
   * +          ),
   * +          delegate = listDelegate
   * +        )
   */
}
