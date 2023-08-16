package com.example.storytellerSampleAndroid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storytellerSampleAndroid.models.Item
import com.example.storytellerSampleAndroid.models.Layout
import com.example.storytellerSampleAndroid.models.VideoRepo
import com.example.storytellerSampleAndroid.models.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class UiState(val items: List<Item>)

class MainActivityViewModel: ViewModel() {

  private val _stateFlow = MutableStateFlow(UiState(items = emptyList()))
  val stateFlow: Flow<UiState> = _stateFlow

  private val videoRepo = VideoRepo()

  private val removeItemFlow = MutableSharedFlow<String>()

  init {
    removeItemFlow
      .onEach { itemId ->
        Log.d("FINA", ": $itemId")
        val items = _stateFlow.value.items.filter { it.id != itemId }
        _stateFlow.value = UiState(items)
      }
      .launchIn(viewModelScope)
  }

  fun reloadData(){
    viewModelScope.launch {
      videoRepo.getVerticalVideosList()
        .data
        .mapNotNull { it.toEntity }
        .map {
          it.forceReload = true
          it.removeItemFlow = removeItemFlow
          it
        }
        .let {
        _stateFlow.value = UiState(items = it)
      }
    }
  }
}