package com.getstoryteller.storytellershowcaseapp.ads.managers.provider.google

import android.content.Context
import android.os.Bundle
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  This class manages the communication with the Google Ad Manager SDK.
 *  Essentially, it receives a request to load a particular ad and then sets up an AdLoader to load the ad.
 *  Once the GAM SDK returns an ad, it returns the NativeCustomFormatAd to the StorytellerAdsDelegate for converstion to the StorytellerAd
 *  class which the Storyteller SDK needs to render the ad
 */
@Singleton
class GoogleNativeAdsManager @Inject constructor(@ApplicationContext private val context: Context) {

  fun requestAd(
    adUnit: String,
    formatId: String,
    customMap: Map<String, String>,
    onAdDataLoaded: (ad: NativeCustomFormatAd) -> Unit,
    onAdDataFailed: (error: String) -> Unit
  ) {
    val adLoader = AdLoader.Builder(context, adUnit).forCustomFormatAd(formatId, { nativeCustomFormatAd ->
        onAdDataLoaded(nativeCustomFormatAd)
      }) { nativeCustomFormatAd, s ->
        Timber.tag("StorytellerAds").d("Click events are handled natively $nativeCustomFormatAd $s")
      }.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
          super.onAdFailedToLoad(loadAdError)
          onAdDataFailed(loadAdError.toString())
        }
      }).build()

    val bundle = baseAdParamBundle

    adLoader.loadAd(AdManagerAdRequest.Builder().apply { customMap.forEach { addCustomTargeting(it.key, it.value) } }
      .addNetworkExtrasBundle(AdMobAdapter::class.java, bundle).build())
  }

  companion object {
    private const val TAG = "NativeAdsManager"

    private val baseAdParamBundle = Bundle().apply {
      //add bundle params
    }
  }
}

