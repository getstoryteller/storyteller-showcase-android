package com.getstoryteller.storytellershowcaseapp.domain

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
    val categories = result.firstOrNull()?.categories.orEmpty()
    val collection = result.find { it.collection.isNullOrEmpty().not() }?.collection.orEmpty()
    sessionRepository.categories = categories
    sessionRepository.collection = collection
  }
}
