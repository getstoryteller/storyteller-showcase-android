package com.example.storytellerSampleAndroid.compose

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import com.example.storytellerSampleAndroid.OtherActivity
import com.example.storytellerSampleAndroid.compose.components.MainContent
import com.example.storytellerSampleAndroid.compose.components.TopBar
import com.example.storytellerSampleAndroid.theme.StorytellerSampleComposeTheme
import com.storyteller.Storyteller
import com.storyteller.domain.AdResponse
import com.storyteller.domain.ClientStory
import com.storyteller.domain.ListDescriptor
import com.storyteller.domain.UserActivity
import com.storyteller.domain.UserActivityData
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.services.Error
import com.storyteller.ui.list.StorytellerDelegate
import com.storyteller.ui.list.StorytellerListViewDelegate
import kotlinx.coroutines.launch

class JetpackComposeActivity : ComponentActivity(), StorytellerDelegate,
  StorytellerListViewDelegate {
  private val viewModel: JetpackComposeViewModel by viewModels()
  private lateinit var controller: StorytellerComposeController

  private fun refresh() = lifecycleScope.launch {
    viewModel.startRefreshing()
    controller.reloadData()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Storyteller.storytellerDelegate = this
    controller = StorytellerComposeController().bind(this)

    setContent {
      StorytellerSampleComposeTheme {
        Scaffold(
          topBar = { TopBar() },
          content = { paddingValues ->
            MainContent(
              paddingValues = paddingValues,
              onRefresh = { refresh() },
              viewModel = viewModel,
              controller = controller,
              delegate = this
            )
          })
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleDeepLink(intent?.data)
  }

  private fun handleDeepLink(data: Uri?) {
    if (data != null) {/*
       If your app needs to open specific story or page e.g. when opening an activity from a deep link,
       then you should call openStory(storyId) or openPage(pageId). It can be tested in the Sample App with adb command:
       adb shell am start -W -a android.intent.action.VIEW -d "https://storytellersampleapp/[PAGE_ID]"
       For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view#Methods
       */
      val pageId = data.lastPathSegment
      Storyteller.openPage(this, pageId) {
        Log.e(
          "Storyteller Sample", "Cannot open deep link $data", it
        )
      }
    }
  }

  /*
  Called when the data loading network request is complete
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#ErrorHandling
   */
  override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
    Log.i(
      "Storyteller Sample",
      "onDataLoadComplete callback: success $success, error $error, dataCount $dataCount"
    )
    viewModel.stopRefreshing()
    if (success) {
      handleDeepLink(intent?.data)
    }
  }

  /*
  Called when the network request to load data for all stories has started
   */
  override fun onDataLoadStarted() {
    Log.i("Storyteller Sample", "onDataLoadStarted callback")
  }

  /*
  Called when story player has been dismissed
   */
  override fun onPlayerDismissed() {
    Log.i("Storyteller Sample", "onPlayerDismissed callback")
  }

  /*
   Called when an analytics event is triggered
   For more info, see - https://www.getstoryteller.com/documentation/android/analytics
   */
  override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
    Log.i("Storyteller Sample", "onUserActivityOccurred: type $type data $data")
  }

  /*
  Called whenever a tile is visible in the story view
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#TileVisibility
   */
  override fun tileBecameVisible(contentIndex: Int) {
    Log.i("Storyteller Sample", "tileBecameVisible: storyIndex $contentIndex")
  }

  /*
  Called when a user swipes up on a page which should direct the user
  to a specific place within the integrating app.
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#SwipingUpToTheIntegratingApp
   */
  override fun userNavigatedToApp(url: String) {
    Log.i("Storyteller Sample", "userNavigatedToApp: swipeUpUrl $url")
    // Pass swipeUpUrl from SDK callback to OtherActivity where it can be accessed as an extra string value when it is started
    startActivity(Intent(this, OtherActivity::class.java).apply {
      putExtra("EXTRA_SWIPE_URL", url)
    })
  }

  /*
 Called when a user swipes up on a page which opens a web link.
 Allows to configure WebViewClient if required.
 For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#HowToUse
  */
  override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) {
    Log.i("Storyteller Sample", "configureWebView $url")
  }

  override fun getAdsForList(
    listDescriptor: ListDescriptor,
    stories: List<ClientStory>,
    onComplete: (AdResponse) -> Unit,
    onError: () -> Unit
  ) {
    Log.i("Storyteller Sample", "getAdsForRow: stories $stories")
  }

  /*
  Called when the tenant is configured to request ads from the containing app
  and the SDK requires ad data from the containing app
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#ClientAds
   */
  override fun getAdsForList(
    stories: List<ClientStory>, onComplete: (AdResponse) -> Unit, onError: () -> Unit
  ) = Unit
}

fun Context.toast(text: String) {
  Handler(Looper.getMainLooper()).post {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
  }
}

