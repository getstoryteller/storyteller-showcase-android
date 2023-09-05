package com.example.storytellerSampleAndroid.ads

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.ADVERTISER_NAME
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.CLICK_CTA
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.CLICK_TYPE
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.CLICK_URL
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.CREATIVE_TYPE
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.DISPLAY
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.IMAGE
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.IN_APP
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.PLAY_STORE_ID
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.STORE
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.TRACKING_URL
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.VIDEO
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.VIDEO_URL
import com.example.storytellerSampleAndroid.ads.AdConstants.Companion.WEB
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.ClipsAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.StoriesAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.domain.entities.ads.StorytellerAdAction
import com.storyteller.remote.ads.StorytellerAdTrackingPixel
import com.storyteller.ui.list.StorytellerDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StorytellerAdsDelegate(
  private val nativeAdsManager: NativeAdsManager
) : StorytellerDelegate {

  companion object {
    const val adUnitId = "/33813572/stories-native-ad-unit"
    const val formatId = "12102683"
  }

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
        "stClipId" to adRequestInfo.itemInfo.id
      )

      nativeAdsManager.requestAd(
        adUnit = adUnitId,
        formatId = formatId,
        customMap = customMap, // custom targeting params
        onAdDataLoaded = { ad ->
          if (ad != null) {
            val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
            if (creativeId != null) {
              val clipsNativeAd = StorytellerNativeAd(adRequestInfo.itemInfo, ad)
              nativeAds[creativeId] = clipsNativeAd
              val storytellerAd = ad.toClientAd(creativeId)

              try {
                if (storytellerAd != null && !storytellerAd.video.isNullOrEmpty()) {
                  onComplete(storytellerAd)
                }
              } catch (ex: Exception) {
                onError()
                Log.w("StorytellerAds", "Failed to send ads to storyteller", ex)
              }
            }
          }
        },
        onAdDataFailed = {
          Log.w("StorytellerAds", "Error when fetching GAM Ad: $it")
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
        "stCurrentCategory" to adRequestInfo.categories.joinToString(separator = ",")
      )
      nativeAdsManager.requestAd(
        adUnit = adUnitId,
        formatId = formatId,
        customMap = customMap, // custom targeting params
        onAdDataLoaded = { ad ->
          if (ad != null) {
            val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
            if (creativeId != null) {
              val storyNativeAd = StorytellerNativeAd(adRequestInfo.itemInfo, ad)
              nativeAds[creativeId] = storyNativeAd
              val storytellerAd = ad.toClientAd(creativeId)

              try {
                if (storytellerAd != null) {
                  onComplete(storytellerAd)
                }
              } catch (ex: Exception) {
                onError()
                Log.w("StorytellerAds", "Failed to send ads to storyteller", ex)
              }
            }
          }
        },
        onAdDataFailed = {
          Log.w("StorytellerAds", "Error when fetching GAM Ad: $it")
        }
      )
    }
  }

  override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) = Unit

  override fun userNavigatedToApp(url: String) = Unit

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
  //endregion

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
    trackAdImpression(data.adId.toString())
    trackAdEnteredView(data.adId.toString(), data.adView)
  }

  private fun onAdEnd(data: UserActivityData) {
    val nativeAd = nativeAds[data.adId]?.nativeAd
    val adView = data.adView
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd != null && adView != null) {
        nativeAd.displayOpenMeasurement?.setView(adView)
      }
    }
  }

  private fun trackAdImpression(adId: String) {
    val nativeAd = nativeAds[adId]?.nativeAd
    storytellerScope.launch(Dispatchers.Main) {
      nativeAd?.recordImpression()
    }
  }

  private fun trackAdEnteredView(adId: String, adView: View?) {
    val nativeAd = nativeAds[adId]?.nativeAd
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd != null && adView != null) {
        nativeAd.displayOpenMeasurement?.apply {
          setView(adView)
          start()
        }
      }
    }
  }

  //region mapping
  private fun createAdAction(
    clickType: String,
    clickThroughUrl: String?,
    clickCTA: String?,
    playStoreId: String?
  ): StorytellerAdAction? = when {
    clickType.equals(
      WEB,
      ignoreCase = true
    ) && clickThroughUrl != null -> StorytellerAdAction.createWebAction(clickThroughUrl, clickCTA)

    clickType.equals(
      IN_APP,
      ignoreCase = true
    ) && clickThroughUrl != null -> StorytellerAdAction.createInAppAction(clickThroughUrl, clickCTA)

    clickType.equals(
      STORE,
      ignoreCase = true
    ) && playStoreId != null -> StorytellerAdAction.createStoreAction(playStoreId, clickCTA)

    else -> null
  }

  private fun NativeCustomFormatAd?.toClientAd(adKey: String) = this?.run {
    val advertiserName = getText(ADVERTISER_NAME)?.toString()
    val creativeType = getText(CREATIVE_TYPE)?.toString()
    val image = getImage(IMAGE)?.uri?.toString()
    val video = getText(VIDEO_URL)?.toString()
    val clickType = getText(CLICK_TYPE)?.toString()
    val clickThroughUrl = getText(CLICK_URL)?.toString()
    val playStoreId = getText(PLAY_STORE_ID)?.toString()
    val clickThroughCTA = getText(CLICK_CTA)?.toString()

    val trackingUrl = getText(TRACKING_URL)?.toString()
    val trackingPixels = mutableListOf<StorytellerAdTrackingPixel>()
    if (trackingUrl != null) {
      trackingPixels.add(
        StorytellerAdTrackingPixel(
          eventType = UserActivity.EventType.OPENED_AD,
          url = trackingUrl
        )
      )
    }

    if (creativeType == DISPLAY && image != null) {
      StorytellerAd.createImageAd(
        id = adKey,
        advertiserName = advertiserName,
        image = image,
        storytellerAdAction = if (!clickType.isNullOrEmpty()) createAdAction(
          clickType = clickType,
          clickThroughUrl = clickThroughUrl,
          clickCTA = clickThroughCTA,
          playStoreId = playStoreId
        ) else {
          null
        },
        trackingPixels = trackingPixels
      )
    } else if (creativeType == VIDEO && video != null) {
      StorytellerAd.createVideoAd(
        id = adKey,
        advertiserName = advertiserName,
        video = video,
        storytellerAdAction = if (!clickType.isNullOrEmpty()) createAdAction(
          clickType = clickType,
          clickThroughUrl = clickThroughUrl,
          clickCTA = clickThroughCTA,
          playStoreId = playStoreId
        ) else {
          null
        },
        trackingPixels = trackingPixels
      )
    } else {
      null
    }
  }

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