package com.getstoryteller.storytellersampleapp.domain

import com.getstoryteller.storytellersampleapp.data.repo.TenantRepository
import com.getstoryteller.storytellersampleapp.features.home.PageItemUiModel

interface GetTabContentUseCase {
  suspend fun getTabContent(tabId: String): List<PageItemUiModel>
}

class GetTabContentUseCaseImpl(private val tenantRepository: TenantRepository) :
  GetTabContentUseCase {
  override suspend fun getTabContent(tabId: String): List<PageItemUiModel> {
    return tenantRepository.getTabForId(tabId)
      .map {
        PageItemUiModel(
          itemId = it.id,
          title = it.title ?: "",
          categories = it.categories,
          collectionId = it.collection ?: "",
          displayLimit = it.displayLimit ?: Int.MAX_VALUE,
          type = it.videoType,
          layout = it.layout,
          tileType = it.tileType,
          size = it.size
        )
      }
  }
}
