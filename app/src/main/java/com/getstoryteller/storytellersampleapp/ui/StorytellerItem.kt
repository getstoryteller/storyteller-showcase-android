package com.getstoryteller.storytellersampleapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    disableHeader: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                                .height(150.dp)
                                .padding(bottom = 16.dp),
                            dataModel = StorytellerStoriesDataModel(
                                theme = Storyteller.theme,
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
                            modifier = Modifier.fillMaxWidth(),
                            dataModel = StorytellerStoriesDataModel(
                                theme = Storyteller.theme,
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
                            modifier = Modifier.fillMaxWidth(),
                            dataModel = StorytellerStoriesDataModel(
                                theme = Storyteller.theme,
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
                                theme = Storyteller.theme,
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
                            modifier = Modifier.fillMaxWidth(),
                            dataModel = StorytellerClipsDataModel(
                                theme = Storyteller.theme,
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
                            modifier = Modifier.fillMaxWidth(),
                            dataModel = StorytellerClipsDataModel(
                                theme = Storyteller.theme,
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