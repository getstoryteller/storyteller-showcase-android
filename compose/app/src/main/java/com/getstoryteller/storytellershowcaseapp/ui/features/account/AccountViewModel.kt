package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.LogoutUseCase
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.storyteller.Storyteller
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val logoutUseCase: LogoutUseCase,
  private val sessionRepository: SessionRepository,
  private val storytellerService: StorytellerService,
  private val amplitudeService: AmplitudeService,
) : ViewModel() {
  val isLoggedOut = MutableStateFlow(false)
  private var debounceJob: Job? = null
  val currentUserId = MutableStateFlow(Storyteller.currentUserId.orEmpty())

  fun changeUserId(
    userId: String = UUID.randomUUID().toString(),
    function: () -> Unit = {},
  ) {
    currentUserId.update { userId }
    debounceJob?.cancel()
    debounceJob = viewModelScope.launch {
      delay(300L)
      sessionRepository.userId = userId
      storytellerService.initStoryteller()
      amplitudeService.init()
      function()
    }
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
}
