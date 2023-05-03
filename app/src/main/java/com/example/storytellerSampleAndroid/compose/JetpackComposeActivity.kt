package com.example.storytellerSampleAndroid.compose

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import com.example.storytellerSampleAndroid.OtherActivity
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel.JetpackComposeViewModelFactory
import com.example.storytellerSampleAndroid.compose.components.MainContent
import com.example.storytellerSampleAndroid.compose.components.TopBar
import com.example.storytellerSampleAndroid.compose.theme.StorytellerSampleComposeTheme
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.domain.entities.Error
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.ui.list.StorytellerListViewDelegate
import kotlinx.coroutines.launch

class JetpackComposeActivity : ComponentActivity(), StorytellerListViewDelegate {

  // will be created from other classes (e.g. StorytellerDiModule)
  private val sampleStorytellerDelegate: SampleStorytellerDelegate = SampleStorytellerDelegateImpl()


  private val viewModel: JetpackComposeViewModel by viewModels {
    // We can inject this at the View leve or the VM level
    // at this example we are using manual DI, so we are injecting it at the VM level
    JetpackComposeViewModelFactory(isDarkMode, sampleStorytellerDelegate)
  }

  private lateinit var controller: StorytellerComposeController

  private fun refresh() = lifecycleScope.launch {
    viewModel.startRefreshing()
    controller.reloadData()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Storyteller.storytellerDelegate = sampleStorytellerDelegate
    controller = StorytellerComposeController().bind(this)

    setContent {
      StorytellerSampleComposeTheme(darkTheme = viewModel.isDarkMode.value) {
        Scaffold(topBar = { TopBar() }, content = { paddingValues ->
          MainContent(
            paddingValues = paddingValues,
            onRefresh = { refresh() },
            viewModel = viewModel,
            controller = controller,
            storytellerListViewDelegate = this,
            onUserNavigatedToApp = { url ->
              startActivity(Intent(this, OtherActivity::class.java).apply {
                putExtra("EXTRA_SWIPE_URL", url)
              })
            }
          )
        })
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleDeepLink(intent?.data)
  }

  override fun onActivityReenter(resultCode: Int, data: Intent?) {
    super.onActivityReenter(resultCode, data)
    activityReentered()
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

  /**
   * Called when the data loading network request is complete
   * For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#ErrorHandling
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

  /**
   * Called when the network request to load data for all stories has started
   */
  override fun onDataLoadStarted() {
    Log.i("Storyteller Sample", "onDataLoadStarted callback")
  }

  /**
   * Called when story player has been dismissed
   */
  override fun onPlayerDismissed() {
    Log.i("Storyteller Sample", "onPlayerDismissed callback")
  }
}

fun Context.toast(text: String) {
  Handler(Looper.getMainLooper()).post {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
  }
}

val Context.isDarkMode get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

