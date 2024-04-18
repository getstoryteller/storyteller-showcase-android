package com.getstoryteller.storytellershowcaseapp.domain

import android.view.Gravity
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiElement
import com.storyteller.domain.entities.theme.builders.UiTheme
import com.storyteller.domain.entities.theme.builders.buildTheme
import com.storyteller.domain.entities.theme.builders.from
import com.storyteller.domain.entities.theme.builders.ofHexCode

private val squareTheme: UiTheme
  get() {
    return buildTheme {
      light.lists.row.startInset = 12
      light.lists.row.endInset = 12
      light.lists.grid.topInset = 0

      light.colors.primary = ofHexCode("#FBCD44")
      light.colors.success = ofHexCode("#3BB327")
      light.colors.alert = ofHexCode("#C8102E")
      light.colors.white.primary = ofHexCode("#FFFFFF")
      light.colors.white.secondary = ofHexCode("#D5D8D9")
      light.colors.white.tertiary = ofHexCode("#8E9196")
      light.colors.black.primary = ofHexCode("#000000")
      light.colors.black.secondary = ofHexCode("#45494C")
      light.colors.black.tertiary = ofHexCode("#4E5356")

      light.lists.backgroundColor = ofHexCode("#F3F4F5")

      light.engagementUnits.poll.selectedAnswerBorderColor = ofHexCode("#B3FFFFFF")

      light.tiles.rectangularTile.unreadIndicator.backgroundColor = ofHexCode("#FBCD44")
      light.tiles.rectangularTile.unreadIndicator.textColor = ofHexCode("#000000")
      light.tiles.rectangularTile.chip.alignment = Gravity.START

      light.tiles.rectangularTile.liveChip.unreadBackgroundColor = ofHexCode("#C8102E")
      light.tiles.rectangularTile.liveChip.readBackgroundColor = ofHexCode("#4E5356")

      light.tiles.circularTile.liveChip.unreadBackgroundColor = ofHexCode("#C8102E")
      light.tiles.circularTile.liveChip.readBackgroundColor = ofHexCode("#4E5356")

      light.tiles.circularTile.title.unreadTextColor = ofHexCode("#000000")
      light.tiles.circularTile.title.readTextColor = ofHexCode("#4E5356")
      light.tiles.circularTile.readIndicatorColor = ofHexCode("#C5C5C5")
      light.tiles.circularTile.unreadIndicatorColor = ofHexCode("#FBCD44")

      light.tiles.title.alignment = Gravity.START
      light.tiles.title.textSize = 13
      light.tiles.title.lineHeight = 13

      light.instructions.button.textColor = ofHexCode("#ffffff")

      dark from light

      dark.tiles.circularTile.title.unreadTextColor = ofHexCode("#FFFFFF")
      dark.lists.backgroundColor = ofHexCode("#000000")
      dark.instructions.button.textColor = ofHexCode("#000000")
    }
  }

private val roundTheme: UiTheme
  get() {
    return squareTheme.copy(
      light = squareTheme.light.copy(
        tiles = squareTheme.light.tiles.copy(
          title = squareTheme.light.tiles.title.copy(
            alignment = Gravity.CENTER,
            titleSize = 10,
            lineHeight = 13,
          ),
          circularTile = squareTheme.light.tiles.circularTile.copy(
            unreadIndicatorColor = ofHexCode("#C8102E"),
          ),
        ),
      ),
      dark = squareTheme.dark.copy(
        tiles = squareTheme.dark.tiles.copy(
          title = squareTheme.dark.tiles.title.copy(
            alignment = Gravity.CENTER,
            titleSize = 10,
            lineHeight = 13,
          ),
          circularTile = squareTheme.dark.tiles.circularTile.copy(
            unreadIndicatorColor = ofHexCode("#C8102E"),
          ),
        ),
      ),
    )
  }

interface GetHomeScreenUseCase {
  suspend fun getHomeItems(): List<UiElement>
}

class GetHomeScreenUseCaseImpl(
  private val tenantRepository: TenantRepository,
  private val sessionRepository: SessionRepository,
) : GetHomeScreenUseCase {
  override suspend fun getHomeItems(): List<UiElement> {
    val result = tenantRepository.getHomePage()
    val collectionForMoments = result.find { it.collection.isNullOrEmpty().not() }?.collection.orEmpty()
    sessionRepository.collection = collectionForMoments
    return result.map {
      it.toUiElement(
        roundTheme = roundTheme,
        squareTheme = squareTheme,
      )
    }
  }
}
