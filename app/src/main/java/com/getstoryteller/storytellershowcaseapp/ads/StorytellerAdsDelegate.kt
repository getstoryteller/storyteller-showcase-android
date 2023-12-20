package com.getstoryteller.storytellershowcaseapp.ads

import android.graphics.Bitmap
import android.webkit.WebView
import com.getstoryteller.storytellershowcaseapp.ads.AdConstants.Companion.IMAGE
import com.getstoryteller.storytellershowcaseapp.ads.AdConstants.Companion.VIDEO_URL
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.Storyteller
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.ClipsAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.StoriesAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.ui.list.StorytellerDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// The Storyteller SDK supports displaying Ads from Google Ad Manager.
// For more information on this please see our public documentation here https://www.getstoryteller.com/documentation/android/ads

@Singleton
class StorytellerAdsDelegate @Inject constructor(
  private val nativeAdsManager: NativeAdsManager,
  private val storytellerAdsTracker: StorytellerAdsTracker,
  private val storytellerAdsMapper: StorytellerAdsMapper
) : StorytellerDelegate {

  companion object {
    const val STORY_ADS_UNIT_ID = "/33813572/stories-native-ad-unit"
    const val STORIES_TEMPLATE_ID = "12102683"
    const val CLIP_ADS_UNIT_ID = "/33813572/clips-native-ad-unit"
    const val CLIPS_TEMPLATE_ID = "12269089"
  }

  // This MutableMap is necessary to keep track of which ads have been
  // requested so that later the relevant methods can be called on them
  // to ensure correct attribution and tracking in GAM.

  private val nativeAds: MutableMap<String, StorytellerNativeAd> = mutableMapOf()
  private val storytellerScope = CoroutineScope(Dispatchers.IO + Job())

  override fun getAd(
    adRequestInfo: StorytellerAdRequestInfo,
    onComplete: (StorytellerAd) -> Unit,
    onError: () -> Unit
  ) {
    when (adRequestInfo) {
      is ClipsAdRequestInfo -> {
        handleClipAds(adRequestInfo, onComplete, onError)
      }

      is StoriesAdRequestInfo -> {
        handleStoryAds(adRequestInfo, onComplete, onError)

      }
    }
  }

  private fun handleClipAds(
    adRequestInfo: ClipsAdRequestInfo,
    onComplete: (StorytellerAd) -> Unit,
    onError: () -> Unit
  ) {

    storytellerScope.launch {
      val customMap = mapOf(
        "stCollection" to adRequestInfo.collection,
        "stClipCategories" to adRequestInfo.itemCategories,
        "stClipId" to adRequestInfo.itemInfo.id,
        "stApiKey" to Storyteller.currentApiKey
      )

      nativeAdsManager.requestAd(
        adUnit = CLIP_ADS_UNIT_ID,
        formatId = CLIPS_TEMPLATE_ID,
        customMap = customMap, // custom targeting params
        onAdDataLoaded = { ad ->
          if (ad != null) {
            val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
            if (creativeId != null) {
              val clipsNativeAd = StorytellerNativeAd(adRequestInfo.itemInfo, ad)
              nativeAds[creativeId] = clipsNativeAd

              val storytellerAd = storytellerAdsMapper.map(ad, creativeId)

              try {
                if (storytellerAd != null && !storytellerAd.video.isNullOrEmpty()) {
                  onComplete(storytellerAd)
                }
              } catch (ex: Exception) {
                onError()
                Timber.tag("GamAds").w(ex, "Failed to send ads to storyteller")
              }
            }
          }
        },
        onAdDataFailed = {
          Timber.tag("GamAds").w("Error when fetching GAM Ad: %s", it)
        }
      )
    }
  }

  private fun handleStoryAds(
    adRequestInfo: StoriesAdRequestInfo,
    onComplete: (StorytellerAd) -> Unit,
    onError: () -> Unit
  ) {
    storytellerScope.launch {
      val customMap = mapOf(
        "stStoryId" to adRequestInfo.itemInfo.id,
        "stCategories" to adRequestInfo.itemCategories,
        "stPlacement" to adRequestInfo.placement,
        "stCurrentCategory" to adRequestInfo.categories.joinToString(separator = ","),
        "stApiKey" to Storyteller.currentApiKey
      )
      nativeAdsManager.requestAd(
        adUnit = STORY_ADS_UNIT_ID,
        formatId = STORIES_TEMPLATE_ID,
        customMap = customMap, // custom targeting params
        onAdDataLoaded = { ad ->
          if (ad != null) {
            val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
            if (creativeId != null) {
              val storyNativeAd = StorytellerNativeAd(adRequestInfo.itemInfo, ad)
              nativeAds[creativeId] = storyNativeAd
              val storytellerAd = storytellerAdsMapper.map(ad, creativeId)

              try {
                if (storytellerAd != null) {
                  onComplete(storytellerAd)
                }
              } catch (ex: Exception) {
                onError()
                Timber.tag("GamAds").w(ex, "Failed to send ads to storyteller")
              }
            }
          }
        },
        onAdDataFailed = {
          Timber.tag("GamAds").w("Error when fetching GAM Ad: %s", it)
        }
      )
    }
  }

  override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) = Unit

  override fun userNavigatedToApp(url: String) = Unit

  // This section of this class ensures that all of the necessary methods which must be called on the NativeCustomFormatAd
  // class in order to ensure tracking is correctly attributed in GAM

  override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
    when (type) {
      UserActivity.EventType.AD_ACTION_BUTTON_TAPPED -> onAdAction(data)
      UserActivity.EventType.OPENED_AD -> onAdStart(data)
      UserActivity.EventType.FINISHED_AD -> onAdEnd(data)
      else -> { /*no op*/
      }
    }
    if (type.isPlayerDismissed) {
      clearNativeAds()
    }
  }

  // This method ensures that clicks are counted correctly in GAM

  private fun onAdAction(data: UserActivityData) {
    val nativeAd = nativeAds[data.adId]?.nativeAd
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd != null) {
        if (nativeAd.getImage(IMAGE)?.uri != null) {
          nativeAd.performClick(IMAGE)
        } else if (nativeAd.getText(VIDEO_URL) != null) {
          nativeAd.performClick(VIDEO_URL)
        }
      }
    }
  }

  private fun onAdStart(data: UserActivityData) {
    val nativeAd = nativeAds[data.adId]?.nativeAd
    storytellerAdsTracker.trackAdImpression(nativeAd)
    storytellerAdsTracker.trackAdEnteredView(nativeAd, data.adView)
  }

  private fun onAdEnd(data: UserActivityData) {
    val nativeAd = nativeAds[data.adId]?.nativeAd
    storytellerAdsTracker.trackAdExitedView(nativeAd, data.adView)
  }

  // This method ensures that impressions are counted correctly in GAM

  // This method ensures that the ad has been marked as viewable when the user interacts with it
  // which is important for the impressions and clicks tracked above to count as valid traffic
  // in GAM.

  private fun clearNativeAds() {
    storytellerScope.launch {
      delay(200)
      nativeAds.values.mapNotNull { it.nativeAd }.forEach { it.destroy() }
      nativeAds.clear()
    }
  }
  //endregion

  private val UserActivity.EventType.isPlayerDismissed
    get() = this == UserActivity.EventType.DISMISSED_AD ||
      this == UserActivity.EventType.DISMISSED_STORY ||
      this == UserActivity.EventType.DISMISSED_CLIP

  /**
   * Data class to help binds native ad to the story it was requested for.
   */
  private data class StorytellerNativeAd(
    val entity: StorytellerAdRequestInfo.ItemInfo,
    val nativeAd: NativeCustomFormatAd?
  )

  private val StorytellerAdRequestInfo.itemCategories
    get() = itemInfo.categories.mapNotNull { it.externalId }.filter { it.isNotEmpty() }
      .joinToString(",")
}

