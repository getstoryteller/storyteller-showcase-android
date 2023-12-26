package com.getstoryteller.storytellershowcaseapp.domain

import android.content.Context
import android.view.Gravity
import androidx.compose.runtime.Stable
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.remote.entities.KeyValueDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.TabDto
import com.storyteller.domain.entities.theme.builders.UiTheme
import com.storyteller.domain.entities.theme.builders.buildTheme
import com.storyteller.domain.entities.theme.builders.from
import com.storyteller.domain.entities.theme.builders.ofHexCode
import java.util.UUID

interface GetConfigurationUseCase {
  suspend fun getConfiguration(): Config
}

class GetConfigurationUseCaseImpl(
  private val tenantRepository: TenantRepository,
  private val context: Context,
) : GetConfigurationUseCase {
  override suspend fun getConfiguration(): Config {
    val settings = tenantRepository.getTenantSettings()
    val languages = tenantRepository.getLanguages()
    val teams = tenantRepository.getTeams()
    val tabs =
      if (settings.tabsEnabled) {
        tenantRepository.getTabs()
      } else {
        emptyList()
      }
    return Config(
      configId = UUID.randomUUID().toString(),
      topLevelCollectionId = settings.topLevelClipsCollection,
      tabsEnabled = settings.tabsEnabled,
      languages = languages,
      teams = teams,
      tabs = tabs,
      roundTheme = roundTheme,
      squareTheme = squareTheme,
    )
  }

  // The look and feel of the Storyteller SDK can be customized by using the Storyteller.theme property
  // on the Storyteller object or by passing a theme to an individual StorytellerListView.
  // There are examples of this in the StorytellerService.kt class, as well as in the
  // StorytellerItem.kt class.
  // For a full list of all the possible customizations possible, please see our public documentation
  // here https://www.getstoryteller.com/documentation/android/themes

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

        light.buttons.textColor = ofHexCode("#FFFFFF")
        light.buttons.backgroundColor = ofHexCode("#000000")

        light.engagementUnits.poll.selectedAnswerBorderColor = ofHexCode("#B3FFFFFF")

        light.storyTiles.rectangularTile.unreadIndicator.backgroundColor = ofHexCode("#FBCD44")
        light.storyTiles.rectangularTile.unreadIndicator.textColor = ofHexCode("#000000")
        light.storyTiles.rectangularTile.chip.alignment = Gravity.START

        light.storyTiles.rectangularTile.liveChip.unreadBackgroundColor = ofHexCode("#C8102E")
        light.storyTiles.rectangularTile.liveChip.readBackgroundColor = ofHexCode("#4E5356")

        light.storyTiles.circularTile.liveChip.unreadBackgroundColor = ofHexCode("#C8102E")
        light.storyTiles.circularTile.liveChip.readBackgroundColor = ofHexCode("#4E5356")

        light.storyTiles.circularTile.title.unreadTextColor = ofHexCode("#000000")
        light.storyTiles.circularTile.title.readTextColor = ofHexCode("#4E5356")
        light.storyTiles.circularTile.readIndicatorColor = ofHexCode("#C5C5C5")
        light.storyTiles.circularTile.unreadIndicatorColor = ofHexCode("#FBCD44")

        light.storyTiles.title.alignment = Gravity.START
        light.storyTiles.title.textSize = 13
        light.storyTiles.title.lineHeight = 13

        light.instructions.button.textColor = ofHexCode("#ffffff")

        dark from light

        dark.storyTiles.circularTile.title.unreadTextColor = ofHexCode("#FFFFFF")
        dark.lists.backgroundColor = ofHexCode("#000000")
        dark.instructions.button.textColor = ofHexCode("#000000")
      }
    }

  private val roundTheme: UiTheme
    get() {
      return squareTheme.copy(
        light =
          squareTheme.light.copy(
            storyTiles =
              squareTheme.light.storyTiles.copy(
                title =
                  squareTheme.light.storyTiles.title.copy(
                    alignment = Gravity.CENTER,
                    titleSize = 10,
                    lineHeight = 13,
                  ),
                circularTile =
                  squareTheme.light.storyTiles.circularTile.copy(
                    unreadIndicatorColor = ofHexCode("#C8102E"),
                  ),
              ),
          ),
        dark =
          squareTheme.dark.copy(
            storyTiles =
              squareTheme.dark.storyTiles.copy(
                title =
                  squareTheme.dark.storyTiles.title.copy(
                    alignment = Gravity.CENTER,
                    titleSize = 10,
                    lineHeight = 13,
                  ),
                circularTile =
                  squareTheme.dark.storyTiles.circularTile.copy(
                    unreadIndicatorColor = ofHexCode("#C8102E"),
                  ),
              ),
          ),
      )
    }
}

@Stable
data class Config(
  val configId: String,
  val topLevelCollectionId: String?,
  val tabsEnabled: Boolean,
  val languages: List<KeyValueDto>,
  val teams: List<KeyValueDto>,
  val tabs: List<TabDto>,
  val roundTheme: UiTheme,
  val squareTheme: UiTheme,
)
