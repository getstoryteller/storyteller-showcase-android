package com.getstoryteller.storytellershowcaseapp.data

import android.graphics.Bitmap
import android.webkit.WebView
import com.getstoryteller.storytellershowcaseapp.data.ads.StorytellerAdsManager
import com.getstoryteller.storytellershowcaseapp.data.amplitude.AmplitudeAnalyticsManager
import com.storyteller.Storyteller
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.UserActivity.EventType
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.ui.list.StorytellerDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowcaseStorytellerDelegate @Inject constructor(
  private val storytellerAdsManager: StorytellerAdsManager,
  private val amplitudeAnalyticsManager: AmplitudeAnalyticsManager,
) : StorytellerDelegate {
  private val storytellerDelegateScope = CoroutineScope(Dispatchers.Main + Job())
  private var navCallback: ((String) -> Unit) = {}

  override fun getAd(
    adRequestInfo: StorytellerAdRequestInfo,
    onComplete: (StorytellerAd) -> Unit,
    onError: () -> Unit,
  ) = storytellerAdsManager.handleAds(adRequestInfo, onComplete, onError)

  override fun configureWebView(
    view: WebView,
    url: String?,
    favicon: Bitmap?,
  ) = Unit

  override fun userNavigatedToApp(
    url: String,
  ) {
    storytellerDelegateScope.launch {
      Storyteller.dismissPlayer(false)
      navCallback(url)
    }
  }

  override fun onUserActivityOccurred(
    type: EventType,
    data: UserActivityData,
  ) {
    storytellerAdsManager.handleAdEvents(type, data)
    amplitudeAnalyticsManager.handleAnalyticsEvents(type, data)
  }

  fun onInterceptNavigation(
    callback: (String) -> Unit,
  ) {
    navCallback = callback
  }
}
