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
import com.example.storytellerSampleAndroid.ads.StorytellerAdsDelegate.StorytellerNativeAd.StorytellerClipsNativeAd
import com.example.storytellerSampleAndroid.ads.StorytellerAdsDelegate.StorytellerNativeAd.StorytellerStoriesNativeAd
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.ClipsAdRequestInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo.StoriesAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.AdResponse
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
    val adUnitId = "/33813572/stories-native-ad-unit"
    val formatId = "12102683"
  }

  private var adsRequested: Int = 0
  private val storiesNativeAds: MutableMap<String, StorytellerStoriesNativeAd> = mutableMapOf()
  private val clipsNativeAds: MutableMap<String, StorytellerClipsNativeAd> = mutableMapOf()
  private val storytellerScope = CoroutineScope(Dispatchers.Main + Job())

  override fun getAdsForList(
    adRequestInfo: StorytellerAdRequestInfo,
    onComplete: (AdResponse) -> Unit,
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
    onComplete: (AdResponse) -> Unit,
    onError: () -> Unit
  ) {
    adsRequested = 0
    val clips = adRequestInfo.clips

    val shouldUpdateClientAds = {
      adsRequested++
      if (adsRequested == clips.size) {
        val adsResponse = clipsNativeAds.mapNotNull { (creativeId, entry) ->
          val id = entry.entity.id
          val ad = entry.nativeAd?.toClientAd(creativeId)
          // Storyteller supports only video ads for clips
          if (ad?.video.isNullOrEmpty()) {
            return@mapNotNull null
          }
          id to ad
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
      for (clip in clips) {

        val customMap = mapOf(
          "stCollection" to adRequestInfo.collection,
          "stClipCategories" to clip.categories.mapNotNull { it.externalId }.joinToString(","),
          "stClipId" to clip.id
        )

        nativeAdsManager.requestAd(
          adUnit = adUnitId,
          formatId = formatId,
          customMap = customMap, // custom targeting params
          onAdDataLoaded = { ad ->
            if (ad != null) {
              val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
              if (creativeId != null) {
                clipsNativeAds[creativeId] = StorytellerClipsNativeAd(clip, ad)
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

  private fun handleStoryAds(
    adRequestInfo: StoriesAdRequestInfo,
    onComplete: (AdResponse) -> Unit,
    onError: () -> Unit
  ) {
    adsRequested = 0
    val stories = adRequestInfo.stories

    val shouldUpdateClientAds = {
      adsRequested++
      if (adsRequested == stories.size) {
        val adsResponse = storiesNativeAds.map { (creativeId, entry) ->
          val id = entry.entity.id
          val ad = entry.nativeAd?.toClientAd(creativeId)
          id to ad
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
          "stStoryId" to story.id,
          "stCategories" to storyCategories,
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
                storiesNativeAds[creativeId] = StorytellerStoriesNativeAd(story, ad)
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

  private fun MutableMap<String, String>.fillClipsKVPs(
    collection: String,
    clipInfo: ClipsAdRequestInfo.ClipInfo
  ): MutableMap<String, String> {
    this["stCollection"] = collection
    this["stClipCategories"] = clipInfo.categories.mapNotNull { it.externalId }.joinToString(",")
    this["stClipId"] = clipInfo.id
    return this
  }

  private fun MutableMap<String, String>.fillStoriesKVPs(
    storyCategories: String,
    story: StoriesAdRequestInfo.StoryInfo,
    adRequestInfo: StoriesAdRequestInfo
  ): MutableMap<String, String> {
    this["storyCat"] = storyCategories
    this["stCategories"] = storyCategories
    this["objid"] = story.id
    this["stStoryId"] = story.id
    this["stPlacement"] = adRequestInfo.placement
    this["stCurrentCategory"] = adRequestInfo.categories.joinToString(",")
    return this
  }

  /**
   * Data class to help binds native ad to the story it was requested for.
   */
  private sealed class StorytellerNativeAd {
    data class StorytellerStoriesNativeAd(
      val entity: StoriesAdRequestInfo.StoryInfo,
      val nativeAd: NativeCustomFormatAd?
    ) : StorytellerNativeAd()

    data class StorytellerClipsNativeAd(
      val entity: ClipsAdRequestInfo.ClipInfo,
      val nativeAd: NativeCustomFormatAd?
    ) : StorytellerNativeAd()
  }
}

