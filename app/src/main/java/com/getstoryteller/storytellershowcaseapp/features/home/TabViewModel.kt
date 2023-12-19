package com.getstoryteller.storytellershowcaseapp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.GetTabContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabViewModel @Inject constructor(
  private val getTabContentUseCase: GetTabContentUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow(TabPageUiState())
  val uiState = _uiState.asStateFlow()

  fun loadTab(tabId: String) {
    viewModelScope.launch {
      val items = getTabContentUseCase.getTabContent(tabId)
      _uiState.update {
        TabPageUiState(
          isRefreshing = false,
          tabItems = items
        )
      }
    }
  }

  private var refreshJob: kotlinx.coroutines.Job? = null
    set(value) {
      field?.cancel()
      field = value
    }

  fun onRefresh() {
    refreshJob = viewModelScope.launch {
      _uiState.update {
        it.copy(isRefreshing = true)
      }
      delay(1000)
      _uiState.update {
        it.copy(isRefreshing = false)
      }
    }
  }

  fun hideStorytellerItem(itemId: String) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          tabItems = it.tabItems.filter { item -> item.itemId != itemId }
        )
      }
    }
  }

}

data class TabPageUiState(
  val isRefreshing: Boolean = false,
  val tabItems: List<PageItemUiModel> = emptyList()
)
