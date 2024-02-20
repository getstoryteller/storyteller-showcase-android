package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.model.CategoriesModel
import com.getstoryteller.storytellershowcaseapp.domain.model.CollectionModel
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository

interface GetHomeScreenUseCase {
  suspend fun getHomeItems()
}

class GetHomeScreenUseCaseImpl(
  private val tenantRepository: TenantRepository,
  private val sessionRepository: SessionRepository,
) : GetHomeScreenUseCase {
  override suspend fun getHomeItems() {
    val result = tenantRepository.getHomePage()
    val categories = result.find { it.title != null }
    val collection = result.find { it.collection.isNullOrEmpty().not() }
    sessionRepository.categories = CategoriesModel(
      title = categories?.title ?: "Moments",
      categories = categories?.categories.orEmpty(),
    )
    sessionRepository.collection = CollectionModel(
      title = collection?.title ?: "Moments",
      collection = collection?.collection.orEmpty(),
    )
  }
}
