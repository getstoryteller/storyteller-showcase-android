package com.getstoryteller.storytellershowcaseapp.ui.features.login

import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewAction
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewEffect
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewState

class LoginContract {

  data class State(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false,
  ) : BaseViewState

  sealed interface Action : BaseViewAction {
    data class Error(val errorMessage: String) : Action
    data object Loading : Action
  }

  sealed interface Effect : BaseViewEffect {
    data object NavigateToMainScreen : Effect
  }
}
