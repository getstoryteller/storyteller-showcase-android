package com.getstoryteller.storytellersampleapp.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellersampleapp.data.TenantSettingsDto
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.domain.GetConfigurationUseCase
import com.getstoryteller.storytellersampleapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellersampleapp.services.SessionService
import com.getstoryteller.storytellersampleapp.services.StorytellerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sessionService: SessionService
) : ViewModel() {

    private var config: Config? = null

    private val _uiState = MutableStateFlow(MainPageUiState())
    val uiState: StateFlow<MainPageUiState> = _uiState.asStateFlow()

    val loginDialogVisible = MutableStateFlow(sessionService.apiKey == null)
    val loginProgress = MutableStateFlow(false)
    val loginError = MutableStateFlow<String?>(null)

    fun setup() {
        _uiState.update { it.copy(isRefreshing = true) }
        if (sessionService.apiKey != null) {
            viewModelScope.launch {
                config = getConfigurationUseCase.getConfiguration()
                _uiState.emit(MainPageUiState(config = config))
            }
        }
    }

    fun verifyCode(code: String) {
        viewModelScope.launch {
            if (code.isEmpty()) {
                loginError.value = "Please enter a code."
                return@launch
            }
            loginProgress.value = true
            try {
                verifyCodeUseCase.verifyCode(code)
                config = getConfigurationUseCase.getConfiguration()
                loginDialogVisible.value = false
                _uiState.emit(MainPageUiState(config = config))
            } catch (ex: Exception) {
                loginError.value =
                    "The access code you entered is incorrect. Please double-check your code and try again."
            } finally {
                loginProgress.value = false
            }
        }
    }

    fun logout() {
        loginDialogVisible.value = true
    }

    fun refreshMainPage() {
        _uiState.update {
            it.copy(isRefreshing = true)
        }
    }
}

data class MainPageUiState(
    val isRefreshing: Boolean = false,
    val config: Config? = null,
)