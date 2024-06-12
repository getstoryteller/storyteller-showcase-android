package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.ui.features.home.PageItemUiModel

interface GetTabContentUseCase {
  suspend fun getTabContent(
    tabId: String,
  ): Set<PageItemUiModel>
}

class GetTabContentUseCaseImpl(private val tenantRepository: TenantRepository) :
  GetTabContentUseCase {
  override suspend fun getTabContent(
    tabId: String,
  ) = tenantRepository.getTabForId(tabId)
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
    }.toSet()
}
