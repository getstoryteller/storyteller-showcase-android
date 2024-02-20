package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard

import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.GetDemoDataUseCase
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.Action
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.Action.Error
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.Action.Loading
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.Action.Success
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.Effect
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val getDemoDataUseCase: GetDemoDataUseCase,
) : BaseViewModel<State, Action, Effect>() {

  override fun setInitialState(): State = State()

  override fun onReduceState(
    action: Action,
  ): State = when (action) {
    is Error -> currentState.copy(
      isError = true,
      errorMessage = action.message,
      isLoading = false,
    )

    is Loading -> currentState.copy(
      isError = false,
      isLoading = true,
    )

    is Success -> currentState.copy(
      isError = false,
      isLoading = false,
      data = action.data,
    )
  }

  fun reload(
    forceDataReload: Boolean = true,
  ) {
    viewModelScope.launch {
      sendAction { Loading }
      delay(1_000L)
      val result = getDemoDataUseCase.getDemoData(forceDataReload, ::onRemoveStorytellerItem)
      sendAction { Success(result) }
    }
  }

  private fun onRemoveStorytellerItem(
    idToRemove: String,
  ) {
    val uiData = state.value.data.filter { it.id != idToRemove }
    sendAction { Success(uiData) }
  }
}
