package com.example.storytellerSampleAndroid.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JetpackComposeViewModel(
  initialUiMode: Boolean
) : ViewModel() {
  private var _refreshing = mutableStateOf(false)
  val refreshing: State<Boolean> get() = _refreshing

  private var _isDarkMode = mutableStateOf(initialUiMode)
  val isDarkMode: State<Boolean> get() = _isDarkMode

  fun startRefreshing() {
    _refreshing.value = true
  }

  fun stopRefreshing() {
    _refreshing.value = false
  }

  fun toggleDarkMode() {
    _isDarkMode.value = !_isDarkMode.value
  }

  class JetpackComposeViewModelFactory(private val initialUiMode: Boolean): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return JetpackComposeViewModel(initialUiMode) as T
    }
  }
}