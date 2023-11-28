package com.getstoryteller.storytellersampleapp

import android.app.Application
import com.getstoryteller.storytellersampleapp.services.StorytellerService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SampleApp : Application() {

  @Inject
  lateinit var storytellerService: StorytellerService

  companion object {
    internal const val PREFS_NAME = "SampleAppPrefs"
  }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    storytellerService.initStoryteller()
  }
}
