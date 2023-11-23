package com.getstoryteller.storytellersampleapp.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellersampleapp.domain.LogoutUseCase
import com.getstoryteller.storytellersampleapp.services.SessionService
import com.getstoryteller.storytellersampleapp.services.StorytellerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val sessionService: SessionService,
    private val storytellerService: StorytellerService
) : ViewModel() {
    val isLoggedOut = MutableStateFlow(false)
    val refreshMainPage = MutableStateFlow(false)

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.logout()
            isLoggedOut.value = true
        }
    }

    fun reset() {
        sessionService.reset()
        storytellerService.initStoryteller()
    }
}