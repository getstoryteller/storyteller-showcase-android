package com.getstoryteller.storytellershowcaseapp.ui.features.login

import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.login.LoginContract.Action.Error
import com.getstoryteller.storytellershowcaseapp.ui.features.login.LoginContract.Action.Loading
import com.getstoryteller.storytellershowcaseapp.ui.features.login.LoginContract.Effect.NavigateToMainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val verifyCodeUseCase: VerifyCodeUseCase,
  private val sessionRepository: SessionRepository,
) : BaseViewModel<LoginContract.State, LoginContract.Action, LoginContract.Effect>() {

  init {
    viewModelScope.launch {
      if (sessionRepository.apiKey != null) {
        sendEffect { NavigateToMainScreen }
      }
    }
  }

  override fun setInitialState(): LoginContract.State = LoginContract.State()

  override fun onReduceState(
    action: LoginContract.Action,
  ): LoginContract.State =
    when (action) {
      is Error -> currentState.copy(
        isError = true,
        errorMessage = action.errorMessage,
        isLoading = false,
      )

      is Loading -> currentState.copy(
        isLoading = true,
        isError = false,
      )
    }

  fun verifyCode(
    code: String,
  ) {
    viewModelScope.launch {
      if (code.isEmpty()) {
        sendAction {
          Error("Please enter a code")
        }
        return@launch
      }
      sendAction { Loading }
      try {
        verifyCodeUseCase.verifyCode(code)
        sendEffect { NavigateToMainScreen }
      } catch (ex: Exception) {
        sendAction {
          Error(
            "The access code you entered is incorrect. Please double-check your code and try again.",
          )
        }
      }
    }
  }
}
