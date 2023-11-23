package com.getstoryteller.storytellersampleapp.domain

import com.getstoryteller.storytellersampleapp.data.repo.TenantRepository
import com.getstoryteller.storytellersampleapp.features.home.PageItemUiModel

interface GetHomeScreenUseCase {
    suspend fun getHomeScreen(): List<PageItemUiModel>
}

class GetHomeScreenUseCaseImpl(
    private val tenantRepository: TenantRepository
) : GetHomeScreenUseCase {

    override suspend fun getHomeScreen(): List<PageItemUiModel> {
        return tenantRepository.getHomePage()
            .map {
                PageItemUiModel(
                    itemId = it.id,
                    title = it.title ?: "",
                    categories = it.categories ?: emptyList(),
                    collectionId = it.collection ?: "",
                    displayLimit = Int.MAX_VALUE,
                    type = it.videoType,
                    layout = it.layout,
                    tileType = it.tileType,
                )
            }
    }
}