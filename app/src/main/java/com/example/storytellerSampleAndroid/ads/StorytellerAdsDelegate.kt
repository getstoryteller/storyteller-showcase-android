package com.example.storytellerSampleAndroid.ads

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.domain.entities.UserActivity
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
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.*
import com.storyteller.remote.dtos.TrackingPixelClientAd
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
    val adUnitId = "/33813572/stories-native-ad-unit"
    val formatId = "12102683"
  }

  private var adsRequested: Int = 0
  private val storiesNativeAds: MutableMap<String, StoryNativeAd> = mutableMapOf()
  private val storytellerScope = CoroutineScope(Dispatchers.Main + Job())

  /**
   * Deprecated
   * ads will be provided by newer overloaded version.
   */
  override fun getAdsForList(
    stories: List<ClientStory>,
    onComplete: (AdResponse) -> Unit,
    onError: () -> Unit
  ) = Unit

  override fun getAdsForList(
    listDescriptor: ListDescriptor,
    stories: List<ClientStory>,
    onComplete: (AdResponse) -> Unit,
    onError: () -> Unit
  ) {
    adsRequested = 0
    val shouldUpdateClientAds = {
      adsRequested++
      if (adsRequested == stories.size) {
        val adsResponse = storiesNativeAds.map { (key, entry) ->
          val storyId = entry.story.id
          val ad = entry.nativeAd?.toClientAd(key)
          storyId to ad
        }.toMap()

        Log.i("StorytellerAds", "getAdsForList COMPLETED: $adsResponse")
        try {
          onComplete(adsResponse)
        } catch (ex: Exception) {
          onError()
          Log.w("StorytellerAds", "Failed to send ads to storyteller", ex)
        }
      } else {
        Log.i("StorytellerAds", "Getting more ads")
      }
    }
    storytellerScope.launch {
      for (story in stories) {
        val storyCategories = story.categories
          .mapNotNull { it.externalId }.filter { it.isNotEmpty() }
          .joinToString(",")

        val customMap = mapOf(
          "storytellerStoryId" to story.id,
          "storytellerCategories" to storyCategories,
          "storytellerPlacement" to listDescriptor.placement,
          "storytellerCurrentCategory" to listDescriptor.categories.joinToString(separator = ",")
        )

        nativeAdsManager.requestAd(
          adUnit = adUnitId,
          formatId = formatId,
          customMap = customMap, // custom targeting params
          onAdDataLoaded = { ad ->
            if (ad != null) {
              val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
              if (creativeId != null) {
                storiesNativeAds[creativeId] = StoryNativeAd(story, ad)
              }
            }
            shouldUpdateClientAds()
          },
          onAdDataFailed = {
            shouldUpdateClientAds()
          }
        )
      }
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
    val nativeAd = storiesNativeAds[data.adId]?.nativeAd
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
    val nativeAd = storiesNativeAds[data.adId]?.nativeAd
    val adView = data.adView
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd != null && adView != null) {
        nativeAd.displayOpenMeasurement?.setView(adView)
      }
    }
  }

  private fun trackAdImpression(adId: String) {
    val nativeAd = storiesNativeAds[adId]?.nativeAd
    storytellerScope.launch(Dispatchers.Main) {
      nativeAd?.recordImpression()
    }
  }

  private fun trackAdEnteredView(adId: String, adView: View?) {
    val nativeAd = storiesNativeAds[adId]?.nativeAd
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
  ): ClientAction? = when {
    clickType.equals(
      WEB,
      ignoreCase = true
    ) && clickThroughUrl != null -> ClientAction.createWebAction(clickThroughUrl, clickCTA)
    clickType.equals(
      IN_APP,
      ignoreCase = true
    ) && clickThroughUrl != null -> ClientAction.createInAppAction(clickThroughUrl, clickCTA)
    clickType.equals(
      STORE,
      ignoreCase = true
    ) && playStoreId != null -> ClientAction.createStoreAction(playStoreId, clickCTA)
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
    val trackingPixels = mutableListOf<TrackingPixelClientAd>()
    if (trackingUrl != null) {
      trackingPixels.add(
        TrackingPixelClientAd(
          eventType = UserActivity.EventType.OPENED_AD,
          url = trackingUrl
        )
      )
    }

    if (creativeType == DISPLAY && image != null) {
      ClientAd.createImageAd(
        id = adKey,
        advertiserName = advertiserName,
        image = image,
        clientAction = if (!clickType.isNullOrEmpty()) createAdAction(
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
      ClientAd.createVideoAd(
        id = adKey,
        advertiserName = advertiserName,
        video = video,
        clientAction = if (!clickType.isNullOrEmpty()) createAdAction(
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

  fun clearNativeAds() {
    storytellerScope.launch {
      delay(200)
      storiesNativeAds.values.mapNotNull { it.nativeAd }.forEach { it.destroy() }
      storiesNativeAds.clear()
    }
  }
  //endregion

  private val UserActivity.EventType.isPlayerDismissed
    get() = this == UserActivity.EventType.DISMISSED_AD ||
            this == UserActivity.EventType.DISMISSED_STORY ||
            this == UserActivity.EventType.DISMISSED_CLIP

  private data class StoryNativeAd(val story: ClientStory, val nativeAd: NativeCustomFormatAd?)
}

