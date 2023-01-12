package com.example.storytellerSampleAndroid.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class JetpackComposeViewModel : ViewModel() {
  private var _refreshing = mutableStateOf(false)
  val refreshing: State<Boolean> get() = _refreshing

  fun startRefreshing() {
    _refreshing.value = true
  }

  fun stopRefreshing() {
    _refreshing.value = false
  }
}