package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.ui.features.home.PageItemUiModel

interface GetHomeScreenUseCase {
  suspend fun getHomeScreen(): List<PageItemUiModel>
}

class GetHomeScreenUseCaseImpl(
  private val tenantRepository: TenantRepository,
) : GetHomeScreenUseCase {
  override suspend fun getHomeScreen(): List<PageItemUiModel> {
    return tenantRepository.getHomePage()
      .map {
        PageItemUiModel(
          itemId = it.id,
          title = it.title ?: "",
          moreButtonTitle = it.moreButtonTitle ?: "More",
          categories = it.categories,
          collectionId = it.collection ?: "",
          displayLimit = it.displayLimit ?: Int.MAX_VALUE,
          type = it.videoType,
          layout = it.layout,
          tileType = it.tileType,
          size = it.size,
        )
      }
  }
}
