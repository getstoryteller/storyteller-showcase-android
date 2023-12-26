package com.getstoryteller.storytellershowcaseapp.data.ads.managers

import android.view.View
import com.getstoryteller.storytellershowcaseapp.data.ads.entity.AdConstants
import com.getstoryteller.storytellershowcaseapp.data.ads.entity.StorytellerGoogleAdInfo
import com.getstoryteller.storytellershowcaseapp.data.ads.entity.StorytellerNativeAd
import com.getstoryteller.storytellershowcaseapp.data.ads.kvps.StorytellerKVPProvider
import com.getstoryteller.storytellershowcaseapp.data.ads.managers.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.data.ads.mapper.StorytellerAdsMapper
import com.getstoryteller.storytellershowcaseapp.data.ads.tracking.StorytellerAdsTracker
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.ads.StorytellerAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class StorytellerNativeAdsManager<KVP : StorytellerAdRequestInfo>(
  private val storytellerKVPProvider: StorytellerKVPProvider<KVP>,
  private val googleNativeAdsManager: GoogleNativeAdsManager,
  private val googleAdInfo: StorytellerGoogleAdInfo,
  private val storytellerAdsMapper: StorytellerAdsMapper,
  private val storytellerAdsTracker: StorytellerAdsTracker,
) {
  /**
   * This MutableMap is necessary to keep track of which ads have been
   * requested so that later the relevant methods can be called on them
   * to ensure correct attribution and tracking in GAM.
   */
  private val nativeAds: MutableMap<String, StorytellerNativeAd> = mutableMapOf()
  private val storytellerScope = CoroutineScope(Dispatchers.IO + Job())

  fun handleAds(
    adRequestInfo: KVP,
    onComplete: (StorytellerAd) -> Unit,
    onError: () -> Unit,
  ) {
    val customMap = storytellerKVPProvider.getKVPs(adRequestInfo)

    googleNativeAdsManager.requestAd(
      adUnit = googleAdInfo.adUnitId,
      formatId = googleAdInfo.templateId,
      customMap = customMap,
      onAdDataLoaded = { ad ->
        val creativeId = ad.getText(AdConstants.CREATIVE_ID)?.toString()
        if (creativeId != null) {
          val clipsNativeAd = StorytellerNativeAd(adRequestInfo.itemInfo, ad)
          nativeAds[creativeId] = clipsNativeAd
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
      },
      onAdDataFailed = {
        Timber.tag("GamAds").w("Error when fetching GAM Ad: %s", it)
      },
    )
  }

  fun cleanNativeAds() {
    storytellerScope.launch {
      delay(200)
      nativeAds.values.mapNotNull { it.nativeAd }.forEach { it.destroy() }
      nativeAds.clear()
    }
  }

  fun trackAdStarted(
    adId: String,
    adView: View?,
  ) {
    val nativeAd = nativeAds[adId]?.nativeAd ?: return
    storytellerAdsTracker.trackAdImpression(nativeAd)
    storytellerAdsTracker.trackAdEnteredView(nativeAd, adView ?: return)
  }

  fun trackAdClicked(adId: String) {
    val nativeAd = nativeAds[adId]?.nativeAd ?: return
    storytellerAdsTracker.trackAdClicked(nativeAd)
  }

  fun trackAdFinished(
    adId: String,
    adView: View?,
  ) {
    val nativeAd = nativeAds[adId]?.nativeAd ?: return
    storytellerAdsTracker.trackAdFinished(nativeAd, adView ?: return)
  }
}
