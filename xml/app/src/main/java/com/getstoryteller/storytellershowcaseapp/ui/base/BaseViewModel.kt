package com.getstoryteller.storytellershowcaseapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

/**
 * S - State
 * A - Action
 * E - Effect
 */
abstract class BaseViewModel<S : BaseViewState, A : BaseViewAction, E : BaseViewEffect> :
  ViewModel() {

  abstract fun setInitialState(): S

  private val initialState: S by lazy { setInitialState() }

  private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
  val state = _state.asStateFlow()

  private val _effects = MutableSharedFlow<E>()
  val effects = _effects.asSharedFlow()

  protected var currentState by Delegates.observable(initialState) { _, _, new ->
    _state.value = new
  }

  protected fun sendEffect(
    effect: () -> E,
  ) {
    viewModelScope.launch { _effects.emit(effect.invoke()) }
  }

  protected fun sendAction(
    action: () -> A,
  ) {
    viewModelScope.launch { _state.emit(onReduceState(action.invoke())) }
  }

  protected abstract fun onReduceState(
    action: A,
  ): S
}

interface BaseViewState

interface BaseViewAction

interface BaseViewEffect

suspend fun <T> Flow<T>.listen(
  collector: (effect: T) -> Unit,
) where T : BaseViewEffect {
  this@listen.onEach { effect -> collector(effect) }.collect()
}
