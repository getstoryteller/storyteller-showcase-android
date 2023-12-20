package com.getstoryteller.storytellershowcaseapp.ads

import android.graphics.Bitmap
import android.webkit.WebView
import com.getstoryteller.storytellershowcaseapp.ads.managers.StorytellerClipsAdsManager
import com.getstoryteller.storytellershowcaseapp.ads.managers.StorytellerStoryAdsManager
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.ClipsAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.StoriesAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivity.EventType.AD_ACTION_BUTTON_TAPPED
import com.storyteller.domain.entities.UserActivity.EventType.FINISHED_AD
import com.storyteller.domain.entities.UserActivity.EventType.OPENED_AD
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.ui.list.StorytellerDelegate
import javax.inject.Inject
import javax.inject.Singleton

// The Storyteller SDK supports displaying Ads from Google Ad Manager.
// For more information on this please see our public documentation here https://www.getstoryteller.com/documentation/android/ads

/**
 * Data class to help binds native ad to the story it was requested for.
 */
data class StorytellerNativeAd(
  val entity: StorytellerAdRequestInfo.ItemInfo, val nativeAd: NativeCustomFormatAd?
)

@Singleton
class StorytellerAdsDelegate @Inject constructor(
  private val storytellerStoryAdsManager: StorytellerStoryAdsManager,
  private val storytellerClipsAdsManager: StorytellerClipsAdsManager
) : StorytellerDelegate {


  override fun getAd(
    adRequestInfo: StorytellerAdRequestInfo, onComplete: (StorytellerAd) -> Unit, onError: () -> Unit
  ) {
    when (adRequestInfo) {
      is ClipsAdRequestInfo -> {
        storytellerClipsAdsManager.handleAds(adRequestInfo, onComplete, onError)
      }

      is StoriesAdRequestInfo -> {
        storytellerStoryAdsManager.handleAds(adRequestInfo, onComplete, onError)
      }
    }
  }

  override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) = Unit

  override fun userNavigatedToApp(url: String) = Unit

  // This section of this class ensures that all of the necessary methods which must be called on the NativeCustomFormatAd
  // class in order to ensure tracking is correctly attributed in GAM

  override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
    val adId = data.adId
    if (adId != null) {
      when (type) {
        AD_ACTION_BUTTON_TAPPED -> {
          storytellerStoryAdsManager.trackAdClicked(adId)
          storytellerClipsAdsManager.trackAdClicked(adId)
        }

        OPENED_AD -> {
          storytellerStoryAdsManager.trackAdStarted(adId, data.adView)
          storytellerClipsAdsManager.trackAdStarted(adId, data.adView)
        }

        FINISHED_AD -> {
          storytellerStoryAdsManager.trackAdFinished(adId, data.adView)
          storytellerClipsAdsManager.trackAdFinished(adId, data.adView)
        }

        else -> { /*no op*/
        }
      }
    }

    if (type.isPlayerDismissed) {
      storytellerStoryAdsManager.cleanNativeAds()
      storytellerClipsAdsManager.cleanNativeAds() }
  }

  // This method ensures that clicks are counted correctly in GAM

  // This method ensures that impressions are counted correctly in GAM

  // This method ensures that the ad has been marked as viewable when the user interacts with it
  // which is important for the impressions and clicks tracked above to count as valid traffic
  // in GAM.

  //endregion

  private val UserActivity.EventType.isPlayerDismissed
    get() = this == UserActivity.EventType.DISMISSED_AD || this == UserActivity.EventType.DISMISSED_STORY || this == UserActivity.EventType.DISMISSED_CLIP

}

