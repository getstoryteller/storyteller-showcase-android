package com.example.storytellerSampleAndroid.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JetpackComposeViewModel(
    initialUiMode: Boolean,
    sampleStorytellerDelegate: SampleStorytellerDelegate
) : ViewModel() {
    private var _refreshing = mutableStateOf(false)
    val refreshing: State<Boolean> get() = _refreshing

    val userNavigatedToApp = sampleStorytellerDelegate.userNavigatedToApp

    private var _isDarkMode = mutableStateOf(initialUiMode)
    val isDarkMode: State<Boolean> get() = _isDarkMode

    fun startRefreshing() {
        _refreshing.value = true
    }

    fun stopRefreshing() {
        viewModelScope.launch {
            // emulate network call
            delay(2000)
            _refreshing.value = false
        }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    class JetpackComposeViewModelFactory(
        private val initialUiMode: Boolean,
        private val storytellerDelegate: SampleStorytellerDelegate
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JetpackComposeViewModel(initialUiMode, storytellerDelegate) as T
        }
    }
}