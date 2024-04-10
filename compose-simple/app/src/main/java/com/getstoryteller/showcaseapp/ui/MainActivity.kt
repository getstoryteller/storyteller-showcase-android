package com.getstoryteller.showcaseapp.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.getstoryteller.showcaseapp.ui.theme.ShowCaseAppTheme
import com.storyteller.Storyteller
import com.storyteller.data.StorytellerStoriesDataModel
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.Error
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.ui.compose.components.lists.row.StorytellerStoriesRow
import com.storyteller.ui.list.StorytellerDelegate
import com.storyteller.ui.list.StorytellerListViewDelegate

class MainActivity : AppCompatActivity(), StorytellerDelegate {
  companion object {
    val STORY_CATEGORY_ID = "euro-top-stories"
    val API_KEY = "YOUR API KEY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val listViewDelegate by remember("test_item_id") {
        val value = object : StorytellerListViewDelegate {
          override fun onPlayerDismissed() = Unit

          override fun onDataLoadComplete(
            success: Boolean,
            error: Error?,
            dataCount: Int,
          ) = Unit

          override fun onDataLoadStarted() = Unit
        }
        mutableStateOf(value)
      }
      Storyteller.storytellerDelegate = this

      ShowCaseAppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          Column(modifier = Modifier.fillMaxSize()) {
            StorytellerStoriesRow(
              modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp),
              dataModel = StorytellerStoriesDataModel(
                cellType = StorytellerListViewCellType.ROUND,
                categories = listOf(STORY_CATEGORY_ID),
              ),
              delegate = listViewDelegate,
              isRefreshing = false,
            )

            Box(
              modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            ) {
              AndroidView(
                factory = { context ->
                  FragmentContainerView(context).apply {
                    fragmentContainerViewId = View.generateViewId()
                    id = fragmentContainerViewId
                  }
                },
              )
            }
          }
        }
      }
    }
  }

  private var fragmentContainerViewId: Int = 0


  override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) = Unit

  override fun getAd(
    adRequestInfo: StorytellerAdRequestInfo,
    onComplete: (StorytellerAd) -> Unit,
    onError: () -> Unit
  ) = Unit

  override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) = Unit

  override fun userNavigatedToApp(url: String) {
    Storyteller.dismissPlayer(false) //or true
    handleNavigation(url)
  }

  private fun handleNavigation(url: String) {
    //start activity or do something for testing purposes
    navigate(url)
    Log.d("Test", "handleNavigation: $url")
  }

  private fun navigate(url: String) {
    //convenience methods to test fragment navigation
    fragmentTest(url)
    //deeplink or intent
    //startActivity(intentForUrl(url))
  }

  private fun intentForUrl(url: String): Intent = Intent(Intent.ACTION_VIEW).apply {
    data = android.net.Uri.parse(url)
  }

  private fun fragmentTest(url: String) {
    supportFragmentManager.beginTransaction()
      .replace(fragmentContainerViewId, storyClipsFragment(url))
      .commitAllowingStateLoss()
  }

  private fun storyClipsFragment(url: String): Fragment {
    return UrlFragment.newInstance(url)
  }
}
