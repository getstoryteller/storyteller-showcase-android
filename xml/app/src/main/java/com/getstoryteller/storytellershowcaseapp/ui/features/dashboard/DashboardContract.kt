package com.getstoryteller.storytellershowcaseapp.ui.features.mainxml

import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewAction
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewEffect
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewState
import com.getstoryteller.storytellershowcaseapp.ui.features.mainxml.adapter.UiElement

class MainFragmentContract {

  data class State(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val data: List<UiElement> = listOf(),
  ) : BaseViewState

  sealed interface Action : BaseViewAction {

    data object Loading : Action

    data class Error(val message: String) : Action

    data class Success(val data: List<UiElement>) : Action
  }

  sealed interface Effect : BaseViewEffect
}
