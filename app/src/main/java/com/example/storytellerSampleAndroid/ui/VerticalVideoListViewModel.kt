package com.example.storytellerSampleAndroid.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.storytellerSampleAndroid.models.GetVideoListUseCase
import com.example.storytellerSampleAndroid.models.Item
import com.example.storytellerSampleAndroid.models.VideoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class UiState(val items: List<Item>)

class VerticalVideoListViewModel(
  private val useCase: GetVideoListUseCase
) : ViewModel() {

  private val _uiStateFlow = MutableStateFlow(UiState(items = emptyList()))
  val uiStateFlow: Flow<UiState> = _uiStateFlow

  private val removeItemFlow = MutableSharedFlow<String>()

  init {
    removeItemFlow
      .onEach { itemId ->
        Log.d("FINA", ": $itemId")
        val items = _uiStateFlow.value.items.filter { it.id != itemId }
        _uiStateFlow.value = UiState(items)
      }
      .launchIn(viewModelScope)
  }

  fun reloadData() {
    viewModelScope.launch {
      val items = useCase.invoke()
        .map {
          it.also {
            it.forceReload = true
            it.removeItemFlow = removeItemFlow
          }
        }
      _uiStateFlow.value = UiState(items = items)
    }
  }

  class Factory(private val useCase: GetVideoListUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(VerticalVideoListViewModel::class.java)) {
        return VerticalVideoListViewModel(useCase = useCase) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
    }

  }
}