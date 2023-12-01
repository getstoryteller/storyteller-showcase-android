package com.getstoryteller.storytellersampleapp.ui

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.LayoutType
import com.getstoryteller.storytellersampleapp.data.TileType
import com.getstoryteller.storytellersampleapp.data.VideoType
import com.getstoryteller.storytellersampleapp.features.home.PageItemStorytellerDelegate
import com.getstoryteller.storytellersampleapp.features.home.PageItemUiModel
import com.storyteller.Storyteller
import com.storyteller.data.StorytellerClipsDataModel
import com.storyteller.data.StorytellerStoriesDataModel
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.StorytellerListViewStyle
import com.storyteller.domain.entities.theme.builders.UiTheme
import com.storyteller.ui.compose.components.lists.grid.StorytellerClipsGrid
import com.storyteller.ui.compose.components.lists.grid.StorytellerStoriesGrid
import com.storyteller.ui.compose.components.lists.row.StorytellerClipsRow
import com.storyteller.ui.compose.components.lists.row.StorytellerStoriesRow
import kotlinx.serialization.json.Json

@Composable
fun StorytellerItem(
  uiModel: PageItemUiModel,
  isRefreshing: Boolean,
  navController: NavController,
  roundTheme: UiTheme?,
  squareTheme: UiTheme?,
  disableHeader: Boolean = false
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    if (uiModel.title.isNotEmpty() && !disableHeader) {
      ListHeader(
        text = uiModel.title,
        collectionId = uiModel.collectionId,
        categories = uiModel.categories,
        onMoreClicked = { collectionId, categories ->
          val serializedModel = Json.encodeToString(uiModel)
          if (!collectionId.isNullOrEmpty()) {
            navController.navigate("moreClips/${serializedModel}")
          } else {
            navController.navigate("moreStories/${serializedModel}")
          }
        }
      )
    }
    when (uiModel.type) {
      VideoType.STORY -> {
        when (uiModel.layout) {
          LayoutType.ROW -> {
            StorytellerStoriesRow(
              modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
              dataModel = StorytellerStoriesDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                },
                uiStyle = StorytellerListViewStyle.AUTO,
                displayLimit = uiModel.displayLimit,
                categories = uiModel.categories,
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId) {},
              isRefreshing = isRefreshing
            )
          }

          LayoutType.GRID -> {
            StorytellerStoriesGrid(
              modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
              dataModel = StorytellerStoriesDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                },
                uiStyle = StorytellerListViewStyle.AUTO,
                displayLimit = uiModel.displayLimit,
                categories = uiModel.categories,
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId) {},
              isScrollable = false,
              isRefreshing = isRefreshing
            )
          }

          LayoutType.SINGLETON -> {
            StorytellerStoriesGrid(
              modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
              dataModel = StorytellerStoriesDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                }?.let {
                  it.copy(
                    light = it.light.copy(
                      lists = it.light.lists.copy(
                        grid = it.light.lists.grid.copy(
                          columns = 1
                        )
                      ),
                      storyTiles = it.light.storyTiles.copy(
                        title = it.light.storyTiles.title.copy(
                          titleSize = 21,
                          alignment = Gravity.START,
                          lineHeight = 24
                        )
                      )
                    ),
                    dark = it.dark.copy(
                      lists = it.dark.lists.copy(
                        grid = it.dark.lists.grid.copy(
                          columns = 1
                        )
                      ),
                      storyTiles = it.dark.storyTiles.copy(
                        title = it.dark.storyTiles.title.copy(
                          titleSize = 21,
                          alignment = Gravity.START,
                          lineHeight = 24
                        )
                      )
                    )
                  )
                },
                uiStyle = StorytellerListViewStyle.AUTO,
                displayLimit = 1,
                categories = uiModel.categories,
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId) {},
              isScrollable = false,
              isRefreshing = isRefreshing
            )
          }
        }
      }

      VideoType.CLIP -> {
        when (uiModel.layout) {
          LayoutType.ROW -> {
            StorytellerClipsRow(
              modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 15.dp),
              dataModel = StorytellerClipsDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                },
                uiStyle = StorytellerListViewStyle.AUTO,
                displayLimit = uiModel.displayLimit,
                collection = uiModel.collectionId ?: "",
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId) {},
              isRefreshing = isRefreshing
            )
          }

          LayoutType.SINGLETON -> {
            StorytellerClipsGrid(
              modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
              dataModel = StorytellerClipsDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                }?.let {
                  it.copy(
                    light = it.light.copy(
                      lists = it.light.lists.copy(
                        grid = it.light.lists.grid.copy(
                          columns = 1
                        )
                      ),
                      storyTiles = it.light.storyTiles.copy(
                        title = it.light.storyTiles.title.copy(
                          titleSize = 21,
                          alignment = Gravity.START,
                          lineHeight = 24
                        )
                      )
                    ),
                    dark = it.dark.copy(
                      lists = it.dark.lists.copy(
                        grid = it.dark.lists.grid.copy(
                          columns = 1
                        )
                      ),
                      storyTiles = it.dark.storyTiles.copy(
                        title = it.dark.storyTiles.title.copy(
                          titleSize = 21,
                          alignment = Gravity.START,
                          lineHeight = 24
                        )
                      )
                    )
                  )
                },
                uiStyle = StorytellerListViewStyle.AUTO,
                displayLimit = 1,
                collection = uiModel.collectionId ?: "",
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              isScrollable = false,
              delegate = PageItemStorytellerDelegate(uiModel.itemId) {},
              isRefreshing = isRefreshing
            )
          }

          LayoutType.GRID -> {
            StorytellerClipsGrid(
              modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
              dataModel = StorytellerClipsDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                },
                uiStyle = StorytellerListViewStyle.AUTO,
                displayLimit = uiModel.displayLimit,
                collection = uiModel.collectionId ?: "",
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              isScrollable = false,
              delegate = PageItemStorytellerDelegate(uiModel.itemId) {},
              isRefreshing = isRefreshing
            )
          }
        }
      }
    }
  }
}
