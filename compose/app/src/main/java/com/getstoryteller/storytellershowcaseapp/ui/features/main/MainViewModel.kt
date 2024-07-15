package com.getstoryteller.storytellershowcaseapp.ui.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.domain.GetConfigurationUseCase
import com.getstoryteller.storytellershowcaseapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.ui.features.account.EVENT_TRACKING
import com.storyteller.Storyteller
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class LoginUiState(
  val isLoggedIn: Boolean = false,
  val loginState: LoginState = LoginState.Idle,
)

sealed class LoginState {
  data object Idle : LoginState()

  data object Loading : LoginState()

  data object Success : LoginState()

  data class Error(val message: String) : LoginState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
  private val verifyCodeUseCase: VerifyCodeUseCase,
  private val getConfigurationUseCase: GetConfigurationUseCase,
  private val sessionRepository: SessionRepository,
) : ViewModel() {
  private var config: Config? = null

  private val _uiState = MutableStateFlow(MainPageUiState())
  val uiState: StateFlow<MainPageUiState> = _uiState.asStateFlow()

  private val _reloadMomentsDataTrigger = MutableSharedFlow<String?>()
  val reloadMomentsDataTrigger: SharedFlow<String?> = _reloadMomentsDataTrigger

  private val _reloadHomeTrigger = MutableLiveData<String>()
  val reloadHomeTrigger: LiveData<String> = _reloadHomeTrigger

  private val _loginUiState = MutableStateFlow(LoginUiState(isLoggedIn = sessionRepository.apiKey != null))
  val loginUiState = _loginUiState.asStateFlow()

  private val momentsDisposedAt = MutableStateFlow<Long?>(null)
  val momentsReloadTimeout: StateFlow<Boolean> =
    momentsDisposedAt.map {
      if (it == null) {
        false
      } else {
        val timeElapsed = System.currentTimeMillis() - it
        val tenMinutesInMillis = 10 * 60 * 1000
        timeElapsed > tenMinutesInMillis
      }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(7000), false)

  fun setup() {
    _uiState.update { it.copy(isMainScreenLoading = true) }
    if (sessionRepository.apiKey != null) {
      viewModelScope.launch {
        config = getConfigurationUseCase.getConfiguration()
        Storyteller.theme = config?.squareTheme
        _uiState.emit(MainPageUiState(config = config))
      }
    }
  }

  fun triggerMomentsReloadData() {
    // open to suggestions for a different way to do this. This requires a unique key for LaunchedEffect in MomentsScreen on every button click
    viewModelScope.launch {
      _reloadMomentsDataTrigger.emit(
        UUID.randomUUID().toString(),
      )
    }
  }

  fun verifyCode(
    code: String,
  ) {
    viewModelScope.launch {
      if (code.isEmpty()) {
        _loginUiState.update {
          it.copy(loginState = LoginState.Error("Please enter a code"))
        }
        return@launch
      }
      _loginUiState.update {
        it.copy(loginState = LoginState.Loading)
      }
      try {
        verifyCodeUseCase.verifyCode(code)
        config = getConfigurationUseCase.getConfiguration()
        _loginUiState.value = LoginUiState(isLoggedIn = true, loginState = LoginState.Success)
        viewModelScope.launch {
          _uiState.emit(MainPageUiState(config = config))
        }
      } catch (ex: Exception) {
        viewModelScope.launch {
          _loginUiState.update {
            it.copy(
              isLoggedIn = false,
              loginState =
              LoginState.Error(
                "The access code you entered is incorrect. Please double-check your code and try again.",
              ),
            )
          }
        }
      }
    }
  }

  fun logout() {
    _uiState.value = MainPageUiState()
    _loginUiState.value = LoginUiState()
  }

  fun refreshMainPage() {
    _reloadHomeTrigger.value = UUID.randomUUID().toString()
  }

  fun clearErrorState() {
    _loginUiState.update {
      it.copy(loginState = LoginState.Idle)
    }
  }

  fun momentsDisposed() {
    momentsDisposedAt.value = System.currentTimeMillis()
  }

  override fun onCleared() {
    momentsDisposedAt.value = null
    super.onCleared()
  }

  var handledDeepLink: String? = null
  fun handleDeeplink(
    deepLinkData: String?,
  ) {
    handledDeepLink = deepLinkData
  }

  fun getOptionTitle(
    option: String,
  ): String {
    if (option == EVENT_TRACKING) {
      return "Allow Event Tracking"
    }
    return config?.let {
      it.attributes.keys.find { key ->
        key.urlName == option
      }?.title ?: ""
    } ?: ""
  }
}

data class MainPageUiState(
  val isHomeRefreshing: Boolean = false,
  val isMainScreenLoading: Boolean = false,
  val config: Config? = null,
)
