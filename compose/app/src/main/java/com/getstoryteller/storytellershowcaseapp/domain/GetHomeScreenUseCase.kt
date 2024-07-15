package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.remote.entities.StorytellerItemApiDto
import com.getstoryteller.storytellershowcaseapp.ui.features.home.ImageItemUiModel
import com.getstoryteller.storytellershowcaseapp.ui.features.home.PageItemUiModel
import com.getstoryteller.storytellershowcaseapp.ui.features.home.VideoItemUiModel

interface GetHomeScreenUseCase {
  suspend fun getHomeScreen(): List<PageItemUiModel>
}

class GetHomeScreenUseCaseImpl(
  private val tenantRepository: TenantRepository,
) : GetHomeScreenUseCase {
  override suspend fun getHomeScreen(): List<PageItemUiModel> {
    return tenantRepository.getHomePage()
      .map {
        when (it) {
          is StorytellerItemApiDto.ImageData -> {
            ImageItemUiModel(
              itemId = it.data.id,
              title = it.data.title,
              url = it.data.url,
              darkModeUrl = it.data.darkModeUrl,
              width = it.data.width,
              height = it.data.height,
              action = it.data.action,
            )
          }

          is StorytellerItemApiDto.VerticalVideoListData -> {
            VideoItemUiModel(
              itemId = it.data.id,
              title = it.data.title ?: "",
              moreButtonTitle = it.data.moreButtonTitle ?: "More",
              categories = it.data.categories,
              collectionId = it.data.collection ?: "",
              displayLimit = it.data.count ?: Int.MAX_VALUE,
              type = it.data.videoType,
              layout = it.data.layout,
              tileType = it.data.tileType,
              size = it.data.size,
            )
          }
        }
      }
  }
}
