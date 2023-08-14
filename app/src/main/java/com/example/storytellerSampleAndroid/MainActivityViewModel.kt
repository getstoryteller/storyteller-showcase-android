package com.example.storytellerSampleAndroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storytellerSampleAndroid.models.Item
import com.example.storytellerSampleAndroid.models.Layout
import com.example.storytellerSampleAndroid.models.VideoRepo
import com.example.storytellerSampleAndroid.models.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class UiState(val items: List<Item>)

class MainActivityViewModel: ViewModel() {

  private val _stateFlow = MutableStateFlow(UiState(items = emptyList()))
  val stateFlow: Flow<UiState> = _stateFlow

  private val videoRepo = VideoRepo()

  fun reloadData(){
    viewModelScope.launch {
      videoRepo.getVerticalVideosList().data.mapNotNull { it.toEntity }.let {
        _stateFlow.value = UiState(items = it)
      }
    }

  }
}