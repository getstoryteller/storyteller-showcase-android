package com.getstoryteller.storytellershowcaseapp.ads.tracking

import android.view.View
import com.getstoryteller.storytellershowcaseapp.ads.AdConstants
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorytellerAdsTracker @Inject constructor() {
  private val storytellerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
  fun trackAdImpression(nativeAd: NativeCustomFormatAd?) {
    storytellerScope.launch(Dispatchers.Main) {
      nativeAd?.recordImpression()
    }
  }

  fun trackAdEnteredView(nativeAd: NativeCustomFormatAd, adView: View) {
    storytellerScope.launch(Dispatchers.Main) {
      nativeAd.displayOpenMeasurement.apply {
        setView(adView)
        start()
      }
    }
  }

  fun trackAdFinished(nativeAd: NativeCustomFormatAd, adView: View) {
    storytellerScope.launch(Dispatchers.Main) {
      nativeAd.displayOpenMeasurement.setView(adView)
    }
  }

  fun trackAdClicked(nativeAd: NativeCustomFormatAd) {
    storytellerScope.launch(Dispatchers.Main) {
      if (nativeAd.getImage(AdConstants.IMAGE)?.uri != null) {
        nativeAd.performClick(AdConstants.IMAGE)
      } else if (nativeAd.getText(AdConstants.VIDEO_URL) != null) {
        nativeAd.performClick(AdConstants.VIDEO_URL)
      }
    }
  }
}
