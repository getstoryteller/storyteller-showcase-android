package com.getstoryteller.storytellersampleapp.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.domain.GetConfigurationUseCase
import com.getstoryteller.storytellersampleapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellersampleapp.services.SessionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
  private val sessionService: SessionService
) : ViewModel() {

  companion object {
    const val DELAY = 5000L
  }
  private var config: Config? = null

  private val _uiState = MutableStateFlow(MainPageUiState())
  val uiState: StateFlow<MainPageUiState> = _uiState.asStateFlow()

  private val _reloadMomentsDataTrigger = MutableLiveData<String>()
  val reloadMomentsDataTrigger: LiveData<String> = _reloadMomentsDataTrigger

  private val _reloadHomeTrigger = MutableLiveData<String>()
  val reloadHomeTrigger: LiveData<String> = _reloadHomeTrigger

  private val _loginUiState = MutableStateFlow(LoginUiState(isLoggedIn = sessionService.apiKey != null))
  val loginUiState = _loginUiState.asStateFlow()
  fun setup() {
    _uiState.update { it.copy(isMainScreenLoading = true) }
    if (sessionService.apiKey != null) {
      viewModelScope.launch {
        config = getConfigurationUseCase.getConfiguration()
        _uiState.emit(MainPageUiState(config = config))
      }
    }
  }

  fun triggerMomentsReloadData() {
    // open to suggestions for a different way to do this. This requires a unique key for LaunchedEffect in MomentsScreen on every button click
    _reloadMomentsDataTrigger.value = UUID.randomUUID().toString()
  }

  fun verifyCode(code: String) {
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
        _loginUiState.update {
          it.copy(loginState = LoginState.Success)
        }
        viewModelScope.launch {
          delay(3000)
          _loginUiState.update {
            it.copy(isLoggedIn = true)
          }
          _uiState.emit(MainPageUiState(config = config))
        }

      } catch (ex: Exception) {
        viewModelScope.launch {
          _loginUiState.update {
            it.copy(loginState = LoginState.Error("The access code you entered is incorrect. Please double-check your code and try again."))
          }
          delay(DELAY)
          _loginUiState.update {
            it.copy(loginState = LoginState.Idle)
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
}

data class MainPageUiState(
  val isHomeRefreshing: Boolean = false,
  val isMainScreenLoading: Boolean = false,
  val config: Config? = null,
)
