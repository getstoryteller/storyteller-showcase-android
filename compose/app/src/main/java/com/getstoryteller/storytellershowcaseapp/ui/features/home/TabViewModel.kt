package com.getstoryteller.storytellershowcaseapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.GetTabContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabViewModel @Inject constructor(
  private val getTabContentUseCase: GetTabContentUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(TabPageUiState())
  val uiState = _uiState.asStateFlow()

  private val tabDisposedAt = MutableStateFlow<Long?>(null)
  val tabReloadTimeout: StateFlow<Boolean> =
    tabDisposedAt.map {
      if (it == null) {
        false
      } else {
        val timeElapsed = System.currentTimeMillis() - it
        val tenMinutesInMillis = 10 * 60 * 1000
        timeElapsed > tenMinutesInMillis
      }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(7000), false)

  fun tabDisposed() {
    tabDisposedAt.value = System.currentTimeMillis()
  }

  fun loadTab(
    tabId: String,
  ) {
    viewModelScope.launch {
      val items = getTabContentUseCase.getTabContent(tabId)
      _uiState.update {
        TabPageUiState(
          isRefreshing = false,
          tabs = items,
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

  fun hideStorytellerItem(
    itemId: String,
  ) {
    viewModelScope.launch {
      _uiState.update {
        val item = it.tabs.firstOrNull { it.itemId == itemId } ?: return@update it
        val newItem = item.copy(isHidden = true)
        it.copy(
          tabs = it.tabs.toMutableList().apply {
            val index = indexOf(item)
            removeAt(index)
            add(index, newItem)
          },
        )
      }
    }
  }

  override fun onCleared() {
    tabDisposedAt.value = null
    super.onCleared()
  }
}

data class TabPageUiState(
  val isRefreshing: Boolean = false,
  val tabs: List<PageItemUiModel> = emptyList(),
)
