package com.getstoryteller.storytellershowcaseapp.ui.features.embedded

import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewModel
import com.getstoryteller.storytellershowcaseapp.ui.features.embedded.EmbeddedContract.Action
import com.getstoryteller.storytellershowcaseapp.ui.features.embedded.EmbeddedContract.Action.Success
import com.getstoryteller.storytellershowcaseapp.ui.features.embedded.EmbeddedContract.Effect
import com.getstoryteller.storytellershowcaseapp.ui.features.embedded.EmbeddedContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmbeddedViewModel @Inject constructor(
  private val sessionRepository: SessionRepository,
) : BaseViewModel<State, Action, Effect>() {

  override fun setInitialState(): State = State()

  override fun onReduceState(
    action: Action,
  ): State =
    when (action) {
      is Success -> currentState.copy(
        collection = action.collection,
      )
    }

  fun loadCollection() {
    val collection = sessionRepository.collection
    sendAction { Success(collection.collection) }
  }
}
