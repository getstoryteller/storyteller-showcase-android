package com.example.storytellerSampleAndroid.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JetpackComposeViewModel : ViewModel() {
  private var _refreshing = mutableStateOf(false)
  val refreshing: State<Boolean> get() = _refreshing

  fun startRefreshing() {
    _refreshing.value = true
  }

  fun stopRefreshing() {
    viewModelScope.launch {
      delay(2000)
      _refreshing.value = false
    }
  }
}