package com.example.storytellerSampleAndroid.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JetpackComposeViewModel(
  sampleStorytellerDelegate: SampleStorytellerDelegate
) : ViewModel() {
  private var _refreshing = mutableStateOf(false)
  val refreshing: State<Boolean> get() = _refreshing

  val userNavigatedToApp = sampleStorytellerDelegate.userNavigatedToApp

  fun startRefreshing() {
    _refreshing.value = true
  }

  fun stopRefreshing() {
    _refreshing.value = false
  }

  class JetpackComposeViewModelFactory(
    private val storytellerDelegate: SampleStorytellerDelegate
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return JetpackComposeViewModel(storytellerDelegate) as T
    }
  }
}