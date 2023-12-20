package com.getstoryteller.storytellershowcaseapp.ads

import android.view.View
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorytellerAdsTracker @Inject constructor(){
  private val storytellerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
  fun trackAdImpression(nativeAd: NativeCustomFormatAd?) {
    storytellerScope.launch(Dispatchers.Main) {
      nativeAd?.recordImpression()
    }
  }

    fun trackAdEnteredView(nativeAd: NativeCustomFormatAd?, adView: View?) {
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd != null && adView != null) {
        nativeAd.displayOpenMeasurement.apply {
          setView(adView)
          start()
        }
      }
    }
  }

    fun trackAdExitedView(nativeAd: NativeCustomFormatAd?, adView: View?) {
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd != null && adView != null) {
        nativeAd.displayOpenMeasurement.setView(adView)
      }
    }
  }
}
