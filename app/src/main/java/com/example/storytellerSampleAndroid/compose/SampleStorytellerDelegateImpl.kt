package com.example.storytellerSampleAndroid.compose

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import com.storyteller.Storyteller
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.AdResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 *  Better to use a DI framework like Dagger/Hilt/Koin to inject this dependency as a singleton
 */
class SampleStorytellerDelegateImpl : SampleStorytellerDelegate {
  private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
  private val _userNavigatedToApp = MutableSharedFlow<String>()
  override val userNavigatedToApp: Flow<String> = _userNavigatedToApp

  /**
   * Called when an analytics event is triggered
   * For more info, see - https://www.getstoryteller.com/documentation/android/analytics
   */
  override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
    Log.i("Storyteller Sample", "onUserActivityOccurred: type $type data $data")
  }

  /**
   * Called when a user swipes up on a page which should direct the user
   * to a specific place within the integrating app.
   * For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#SwipingUpToTheIntegratingApp
   */
  override fun userNavigatedToApp(url: String) {
    Log.i("Storyteller Sample", "userNavigatedToApp: swipeUpUrl $url")
    Storyteller.dismissPlayer(false)
    coroutineScope.launch {
      _userNavigatedToApp.emit(url)
    }
    // Pass swipeUpUrl from SDK callback to OtherActivity where it can be accessed as an extra string value when it is started
  }


  /**
   * Called when the tenant is configured to request ads from the containing app
   * and the SDK requires ad data from the containing app
   * For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#ClientAds
   */
  override fun getAdsForList(
    adRequestInfo: StorytellerAdRequestInfo,
    onComplete: (AdResponse) -> Unit,
    onError: () -> Unit
  ) {
    Log.i("Storyteller Sample", "getAdsForRow: stories $adRequestInfo")
  }

  /**
   * Called when a user swipes up on a page which opens a web link.
   * Allows to configure WebViewClient if required.
   * For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#HowToUse
   */
  override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) {
    Log.i("Storyteller Sample", "configureWebView $url")
  }
}