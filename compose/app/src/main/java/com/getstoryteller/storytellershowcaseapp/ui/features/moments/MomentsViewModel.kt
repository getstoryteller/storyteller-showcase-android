package com.getstoryteller.storytellershowcaseapp.ui.features.moments

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MomentsViewModel @Inject constructor() : ViewModel() {
  private val momentsPausedAt = MutableStateFlow<Long?>(null)
  fun scheduleMomentsRefresh() {
    momentsPausedAt.value = System.currentTimeMillis()
  }

  fun shouldReloadMomentsData(): Boolean {
    val pausedAt = momentsPausedAt.value
    val thirtyMinutesInMillis = 2 * 60 * 1000
    momentsPausedAt.value = null
    return pausedAt != null && (System.currentTimeMillis() - pausedAt) > thirtyMinutesInMillis
  }
}
