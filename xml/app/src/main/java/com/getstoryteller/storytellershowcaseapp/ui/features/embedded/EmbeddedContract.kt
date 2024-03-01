package com.getstoryteller.storytellershowcaseapp.ui.features.embedded

import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewAction
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewEffect
import com.getstoryteller.storytellershowcaseapp.ui.base.BaseViewState

class EmbeddedContract {

  data class State(
    val collection: String = "",
  ) : BaseViewState

  sealed interface Action : BaseViewAction {

    data class Success(val collection: String) : Action
  }

  sealed interface Effect : BaseViewEffect
}
