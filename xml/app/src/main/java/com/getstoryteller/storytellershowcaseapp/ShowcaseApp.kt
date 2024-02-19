package com.getstoryteller.storytellershowcaseapp

import android.app.Application
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

// This sample application shows how to fetch information about which Stories and Clips content to display
// from an external API and then how to render these using the Storyteller SDK.
// The ApiService class is used to fetch this information from the API.

// We recommend initializing the Storyteller SDK as near as possible to when your app starts.
// This happens inside the StorytellerService in this sample.

@HiltAndroidApp
class ShowcaseApp : Application() {

  @Inject
  lateinit var storytellerService: StorytellerService

  @Inject
  lateinit var amplitudeService: AmplitudeService

  @Inject
  lateinit var timberTree: Timber.Tree

  companion object {
    internal const val PREFS_NAME = "ShowcaseAppPrefs"
    const val CLIP_COLLECTION = "rapid-replay"
  }

  override fun onCreate() {
    super.onCreate()
    storytellerService.initStoryteller()
    amplitudeService.init()
    MobileAds.initialize(this)
    Timber.plant(timberTree)
  }
}
