package com.example.storytellerSampleAndroid.theme

import android.content.Context
import android.view.Gravity
import com.storyteller.domain.entities.theme.builders.buildTheme
import com.storyteller.domain.entities.theme.builders.from
import com.storyteller.domain.entities.theme.builders.ofHexCode

/**
 * // https://www.getstoryteller.com/documentation/android/themes
 */
object StorytellerThemes {

  fun getGlobalTheme(context: Context) = buildTheme(context) {
      light{
        colors.primary = ofHexCode("#0000FF")
        //font = ResourcesCompat.getFont(context, R.font.your_custom_font)
        primitives.cornerRadius = 4
        lists.row.tileSpacing = 8
        storyTiles.title.alignment = Gravity.CENTER
        storyTiles.rectangularTile.chip.alignment = Gravity.START
        player.showShareButton = true
        buttons.cornerRadius = 4
        instructions.show = true
        engagementUnits.poll.showVoteCount = true
      }
      dark from light // use light theme in dark mode this can also be modified with uiMode parameter on the views
  }

  fun getCustomTheme(context: Context) = buildTheme(context) {
    light{
      colors.primary = ofHexCode("#FF00FF")
      //font = ResourcesCompat.getFont(context, R.font.your_custom_font)
      primitives.cornerRadius = 4
      lists.row.tileSpacing = 8
      storyTiles.title.alignment = Gravity.END
      storyTiles.rectangularTile.chip.alignment = Gravity.CENTER
      player.showShareButton = true
      buttons.cornerRadius = 4
      instructions.show = true
      engagementUnits.poll.showVoteCount = true
    }
    dark from light // use light theme in dark mode this can also be modified with uiMode parameter on the views
  }
}