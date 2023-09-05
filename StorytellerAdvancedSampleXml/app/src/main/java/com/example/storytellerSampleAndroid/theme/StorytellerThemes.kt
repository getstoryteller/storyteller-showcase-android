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
        storyTiles.title.alignment = Gravity.START
        storyTiles.rectangularTile.chip.alignment = Gravity.START
        player.showShareButton = true
        buttons.cornerRadius = 4
        instructions.show = true
        engagementUnits.poll.showVoteCount = true
        storyTiles.title.textSize = 16
      }
      dark from light // use light theme in dark mode this can also be modified with uiMode parameter on the views
  }

  fun getRoundTheme(context: Context) = buildTheme(context) {
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
      storyTiles.title.textSize = 16
    }
    dark from light // use light theme in dark mode this can also be modified with uiMode parameter on the views
  }

  fun getSingletonTheme(context: Context) = buildTheme(context) {
    light{
      colors.primary = ofHexCode("#0000FF")
      //font = ResourcesCompat.getFont(context, R.font.your_custom_font)
      primitives.cornerRadius = 4
      lists.row.tileSpacing = 8
      lists.grid.columns = 1
      lists.row.startInset = 16
      lists.row.endInset = 16
      storyTiles.title.alignment = Gravity.START
      storyTiles.rectangularTile.chip.alignment = Gravity.START
      storyTiles.title.textSize = 16
      player.showShareButton = true
      buttons.cornerRadius = 4
      instructions.show = true
      engagementUnits.poll.showVoteCount = true

    }
    dark from light // use light theme in dark mode this can also be modified with uiMode parameter on the views
  }
}