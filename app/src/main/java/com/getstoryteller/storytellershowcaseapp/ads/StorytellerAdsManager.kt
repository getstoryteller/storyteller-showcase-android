package com.getstoryteller.storytellershowcaseapp.ads

import com.getstoryteller.storytellershowcaseapp.ads.managers.StorytellerClipsNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.ads.managers.StorytellerStoryNativeAdsManager
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.StorytellerAd
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The Storyteller SDK supports displaying Ads from Google Ad Manager.
 * For more information on this please see our public documentation here https://www.getstoryteller.com/documentation/android/ads
 */
@Singleton
class StorytellerAdsManager @Inject constructor(
  private val storytellerStoryAdsManager: StorytellerStoryNativeAdsManager,
  private val storytellerClipsAdsManager: StorytellerClipsNativeAdsManager
) {
    fun handleAds(adRequestInfo: StorytellerAdRequestInfo, onComplete: (StorytellerAd) -> Unit, onError: () -> Unit) {
    when (adRequestInfo) {
      is StorytellerAdRequestInfo.ClipsAdRequestInfo -> {
        storytellerClipsAdsManager.handleAds(adRequestInfo, onComplete, onError)
      }

      is StorytellerAdRequestInfo.StoriesAdRequestInfo -> {
        storytellerStoryAdsManager.handleAds(adRequestInfo, onComplete, onError)
      }
    }
  }

    /**
   * This section of this class ensures that all of the necessary methods which must be called on the NativeCustomFormatAd
   * class in order to ensure tracking is correctly attributed in GAM
   */
  fun handleAdEvents(type: UserActivity.EventType, data: UserActivityData) {
    val adId = data.adId
    if (adId != null) {
      when (type) {
        UserActivity.EventType.AD_ACTION_BUTTON_TAPPED -> {
          storytellerStoryAdsManager.trackAdClicked(adId)
          storytellerClipsAdsManager.trackAdClicked(adId)
        }

        UserActivity.EventType.OPENED_AD -> {
          storytellerStoryAdsManager.trackAdStarted(adId, data.adView)
          storytellerClipsAdsManager.trackAdStarted(adId, data.adView)
        }

        UserActivity.EventType.FINISHED_AD -> {
          storytellerStoryAdsManager.trackAdFinished(adId, data.adView)
          storytellerClipsAdsManager.trackAdFinished(adId, data.adView)
        }

        else -> { /*no op*/
        }
      }
    }

    if (type.isPlayerDismissed) {
      storytellerStoryAdsManager.cleanNativeAds()
      storytellerClipsAdsManager.cleanNativeAds()
    }
  }

  private val UserActivity.EventType.isPlayerDismissed
    get() = this == UserActivity.EventType.DISMISSED_AD || this == UserActivity.EventType.DISMISSED_STORY || this == UserActivity.EventType.DISMISSED_CLIP

}
