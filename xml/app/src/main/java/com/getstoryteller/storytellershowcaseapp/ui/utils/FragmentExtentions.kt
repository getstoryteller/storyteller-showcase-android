package com.getstoryteller.storytellershowcaseapp.ui.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun Fragment.observeOnState(
  state: Lifecycle.State,
  crossinline block: suspend CoroutineScope.() -> Unit,
) {
  viewLifecycleOwner.lifecycleScope.launch {
    repeatOnLifecycle(state) {
      block.invoke(this)
    }
  }
}
