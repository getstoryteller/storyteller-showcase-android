package com.example.storytellerSampleAndroid.ads

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.NativeCustomFormatAd

class NativeAdsManager(private val context: Context) {

  fun requestAd(
    adUnit: String,
    formatId: String,
    customMap: Map<String, String>,
    onAdDataLoaded: (ad: NativeCustomFormatAd?) -> Unit,
    onAdDataFailed: (error: String) -> Unit
  ) {
    val adLoader = AdLoader.Builder(context, adUnit)
      .forCustomFormatAd(
        formatId,
        { nativeCustomFormatAd ->
          onAdDataLoaded(nativeCustomFormatAd)
        })
      { nativeCustomFormatAd, s ->
        Log.d("StorytellerAds", "Click events are handled natively $nativeCustomFormatAd $s")
      }
      .withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
          super.onAdFailedToLoad(loadAdError)
          onAdDataFailed(loadAdError.toString())
        }
      })
      .build()

    val bundle = baseAdParamBundle

    adLoader.loadAd(
      AdManagerAdRequest.Builder()
        .apply { customMap.forEach { addCustomTargeting(it.key, it.value) } }
        .addNetworkExtrasBundle(AdMobAdapter::class.java, bundle)
        .build()
    )
  }

  companion object {
    private const val TAG = "NativeAdsManager"

    private val baseAdParamBundle = Bundle().apply {
      //add bundle params
    }
  }
}

