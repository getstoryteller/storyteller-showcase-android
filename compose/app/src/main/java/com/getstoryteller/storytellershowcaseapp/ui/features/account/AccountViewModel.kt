package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.LogoutUseCase
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.storyteller.Storyteller
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val logoutUseCase: LogoutUseCase,
  private val sessionRepository: SessionRepository,
  private val storytellerService: StorytellerService,
  private val amplitudeService: AmplitudeService,
) : ViewModel() {
  val isLoggedOut = MutableStateFlow(false)

  private val _analyticsUiState = MutableStateFlow(
    Triple(
      sessionRepository.allowPersonalization,
      sessionRepository.allowStoryTellerTracking,
      sessionRepository.allowUserActivityTracking,
    ),
  )
  val analyticsUiState = _analyticsUiState.asStateFlow()

  fun changeUserId(
    userId: String,
  ) = viewModelScope.launch {
    sessionRepository.userId = userId
    storytellerService.initStoryteller()
    amplitudeService.init()
    updateAnalyticsOption()
  }

  fun logout() {
    viewModelScope.launch {
      logoutUseCase.logout()
      isLoggedOut.value = true
    }
  }

  fun reset() {
    sessionRepository.reset()
    storytellerService.initStoryteller()
    amplitudeService.init()
  }

  fun updateAnalyticsOption(
    index: Int = -1,
    newValue: Boolean = true,
  ) {
    when (index) {
      0 -> sessionRepository.allowPersonalization = newValue
      1 -> sessionRepository.allowStoryTellerTracking = newValue
      2 -> sessionRepository.allowUserActivityTracking = newValue
      -1 -> {
        sessionRepository.allowPersonalization = newValue
        sessionRepository.allowStoryTellerTracking = newValue
        sessionRepository.allowUserActivityTracking = newValue
      }
    }
    _analyticsUiState.value = Triple(
      sessionRepository.allowPersonalization,
      sessionRepository.allowStoryTellerTracking,
      sessionRepository.allowUserActivityTracking,
    )
  }
}
