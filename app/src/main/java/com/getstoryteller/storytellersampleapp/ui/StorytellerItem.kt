package com.getstoryteller.storytellersampleapp.ui

import android.view.Gravity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.data.entities.ItemSize
import com.getstoryteller.storytellersampleapp.data.entities.LayoutType
import com.getstoryteller.storytellersampleapp.data.entities.TileType
import com.getstoryteller.storytellersampleapp.data.entities.VideoType
import com.getstoryteller.storytellersampleapp.features.home.PageItemStorytellerDelegate
import com.getstoryteller.storytellersampleapp.features.home.PageItemUiModel
import com.storyteller.data.StorytellerClipsDataModel
import com.storyteller.data.StorytellerStoriesDataModel
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.StorytellerListViewStyle
import com.storyteller.domain.entities.theme.builders.UiTheme
import com.storyteller.ui.compose.components.lists.grid.StorytellerClipsGrid
import com.storyteller.ui.compose.components.lists.grid.StorytellerStoriesGrid
import com.storyteller.ui.compose.components.lists.row.StorytellerClipsRow
import com.storyteller.ui.compose.components.lists.row.StorytellerStoriesRow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// These methods take the FeedItem response from our sample API and uses
// it to construct the relevant Storyteller Row or Grid views.

// The StorytellerStoriesRow and StorytellerStoriesGrid views accept a dataModel
// to determine how they look and behave.
// For more information on the various properties which can be passed here, please see our public
// documentation which is available here https://www.getstoryteller.com/documentation/android/storyteller-list-views

// The delegate parameter which is passed to the StorytellerStoriesRow and StorytellerStoriesGrid
// allows your code to receive notifications about how data loading is progressing.
// In this case, we show a common pattern which is hiding the relevant
// row or grid (and it's corresponding title) when there are no items to
// render in the Storyteller row/grid.
// For more information on this pattern, please see our public documentation here
// https://www.getstoryteller.com/documentation/android/storyteller-list-views

@Composable
fun StorytellerItem(
  uiModel: PageItemUiModel,
  isRefreshing: Boolean,
  navController: NavController,
  roundTheme: UiTheme?,
  squareTheme: UiTheme?,
  disableHeader: Boolean = false,
  isScrollable: Boolean = false,
  onComplete : (String) -> Unit = {}
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    val uiStyle = if (isSystemInDarkTheme()) {
      StorytellerListViewStyle.DARK
    } else {
      StorytellerListViewStyle.LIGHT
    }
    if (uiModel.title.isNotEmpty() && !disableHeader) {
      ListHeader(
        text = uiModel.title,
        moreButtonTitle = uiModel.moreButtonTitle,
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
            val bottomPadding = remember {
              if (uiModel.tileType == TileType.ROUND) {
                36.dp
              } else {
                0.dp
              }
            }
            StorytellerStoriesRow(
              modifier = Modifier
                .fillMaxWidth()
                .height(uiModel.getRowHeight())
                .padding(top = 8.dp, bottom = bottomPadding),
              dataModel = StorytellerStoriesDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                },
                uiStyle = uiStyle,
                displayLimit = uiModel.displayLimit,
                categories = uiModel.categories,
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId),
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
                uiStyle = uiStyle,
                displayLimit = if (isScrollable) Int.MAX_VALUE else uiModel.displayLimit,
                categories = uiModel.categories,
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId, onComplete),
              isScrollable = isScrollable,
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
                          topInset = 0,
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
                          topInset = 0,
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
                uiStyle = uiStyle,
                displayLimit = 1,
                categories = uiModel.categories,
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId, onComplete),
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
                .height(uiModel.getRowHeight())
                .padding(bottom = 15.dp),
              dataModel = StorytellerClipsDataModel(
                theme = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> squareTheme
                  TileType.ROUND -> roundTheme
                },
                uiStyle = uiStyle,
                displayLimit = uiModel.displayLimit,
                collection = uiModel.collectionId ?: "",
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              delegate = PageItemStorytellerDelegate(uiModel.itemId, onComplete),
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
                          topInset = 0,
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
                          topInset = 0,
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
                uiStyle = uiStyle,
                displayLimit = 1,
                collection = uiModel.collectionId ?: "",
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              isScrollable = false,
              delegate = PageItemStorytellerDelegate(uiModel.itemId, onComplete),
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
                uiStyle = uiStyle,
                displayLimit = if (isScrollable) Int.MAX_VALUE else uiModel.displayLimit,
                collection = uiModel.collectionId ?: "",
                cellType = when (uiModel.tileType) {
                  TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
                  TileType.ROUND -> StorytellerListViewCellType.ROUND
                }
              ),
              isScrollable = isScrollable,
              delegate = PageItemStorytellerDelegate(uiModel.itemId, onComplete),
              isRefreshing = isRefreshing
            )
          }
        }
      }
    }
  }
}


fun PageItemUiModel.getRowHeight(): Dp {
  if (this.tileType == TileType.ROUND) {
    return 113.dp
  }
  return when (this.size) {
    ItemSize.SMALL -> 106.dp
    ItemSize.MEDIUM -> 330.dp
    ItemSize.LARGE -> 440.dp
    ItemSize.REGULAR -> 220.dp
  }
}
