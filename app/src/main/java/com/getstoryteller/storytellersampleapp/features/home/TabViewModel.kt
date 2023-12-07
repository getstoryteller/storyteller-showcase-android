package com.getstoryteller.storytellersampleapp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellersampleapp.data.TabDto
import com.getstoryteller.storytellersampleapp.domain.GetTabContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabViewModel @Inject constructor(
  private val getTabContentUseCase: GetTabContentUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow(TabPageUiState())
  val uiState: StateFlow<TabPageUiState> = _uiState.asStateFlow()

  fun loadTab(tabId: String) {
    viewModelScope.launch {
      val items = getTabContentUseCase.getTabContent(tabId)
      _uiState.emit(
        TabPageUiState(
          isRefreshing = true,
          tabItems = items
        )
      )
      _uiState.update {
        it.copy(isRefreshing = false)
      }
    }
  }

  fun onRefresh() {
    viewModelScope.launch {
      _uiState.update {
        it.copy(isRefreshing = true)
      }
      delay(1000)
      _uiState.update {
        it.copy(isRefreshing = false)
      }
    }
  }

}

data class TabPageUiState(
  val isRefreshing: Boolean = false,
  val tabItems: List<PageItemUiModel> = emptyList()
)
