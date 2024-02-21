package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiElement

interface GetHomeScreenUseCase {
  suspend fun getHomeItems(): List<UiElement>
}

class GetHomeScreenUseCaseImpl(
  private val tenantRepository: TenantRepository,
  private val sessionRepository: SessionRepository,
) : GetHomeScreenUseCase {
  override suspend fun getHomeItems(): List<UiElement> {
    val result = tenantRepository.getHomePage()
    val collectionForMoments = result.find { it.collection.isNullOrEmpty().not() }?.collection.orEmpty()
    sessionRepository.collection = collectionForMoments
    return result.map {
      it.toUiElement()
    }
  }
}
