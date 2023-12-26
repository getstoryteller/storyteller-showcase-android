package com.getstoryteller.storytellershowcaseapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.domain.GetHomeScreenUseCase
import com.getstoryteller.storytellershowcaseapp.remote.entities.ItemSize
import com.getstoryteller.storytellershowcaseapp.remote.entities.LayoutType
import com.getstoryteller.storytellershowcaseapp.remote.entities.TabDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.TileType
import com.getstoryteller.storytellershowcaseapp.remote.entities.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
  @Inject
  constructor(
    private val getHomeScreenUseCase: GetHomeScreenUseCase,
  ) : ViewModel() {
    private val _uiState = MutableStateFlow(HomePageUiState())
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()
    private lateinit var config: Config

    fun loadHomePage(config: Config) {
      this.config = config
      onRefresh()
    }

    fun onRefresh() {
      viewModelScope.launch {
        _uiState.update {
          it.copy(isRefreshing = true)
        }

        val homeItems = if (!config.tabsEnabled) getHomeScreenUseCase.getHomeScreen() else listOf()
        _uiState.value =
          HomePageUiState(
            isRefreshing = false,
            tabsEnabled = config.tabsEnabled,
            homeItems = homeItems,
            tabs = config.tabs,
          )
      }
    }

    fun hideStorytellerItem(itemId: String) {
      viewModelScope.launch {
        _uiState.update {
          it.copy(
            homeItems = it.homeItems.filter { item -> item.itemId != itemId },
          )
        }
      }
    }
  }

data class HomePageUiState(
  val isRefreshing: Boolean = false,
  val tabsEnabled: Boolean = false,
  val homeItems: List<PageItemUiModel> = emptyList(),
  val tabs: List<TabDto> = emptyList(),
)

@Serializable
data class PageItemUiModel(
  val itemId: String,
  val type: VideoType,
  val layout: LayoutType,
  val tileType: TileType,
  val title: String,
  val moreButtonTitle: String,
  val categories: List<String>,
  val displayLimit: Int,
  val collectionId: String?,
  val size: ItemSize,
)
