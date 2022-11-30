package com.example.storytellerSampleAndroid

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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.storytellerSampleAndroid.theme.StorytellerSampleComposeTheme
import com.storyteller.Storyteller
import com.storyteller.domain.AdResponse
import com.storyteller.domain.ClientStory
import com.storyteller.domain.StorytellerListViewCellType
import com.storyteller.domain.UserActivity
import com.storyteller.domain.UserActivityData
import com.storyteller.sdk.compose.StorytellerComposeController
import com.storyteller.sdk.compose.StorytellerGridView
import com.storyteller.sdk.compose.StorytellerRowView
import com.storyteller.sdk.compose.composeVersion
import com.storyteller.services.Error
import com.storyteller.ui.list.StorytellerDelegate
import com.storyteller.ui.list.StorytellerListViewDelegate
import java.util.UUID
import kotlinx.coroutines.launch

class JetpackComposeActivity : ComponentActivity(), StorytellerDelegate,
  StorytellerListViewDelegate {
  private val viewModel: JetpackComposeViewModel by viewModels()
  private lateinit var storytellerComposeController: StorytellerComposeController

  private fun refresh() = lifecycleScope.launch {
    viewModel.startRefreshing()
    storytellerComposeController.onEach(refreshNotNeeded = {
      viewModel.stopRefreshing()
    }) {
      reloadData()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Storyteller.storytellerDelegate = this
    storytellerComposeController = StorytellerComposeController().bind(this)

    setContent {
      StorytellerSampleComposeTheme {
        Scaffold(topBar = { TopBar() }) { paddingValues ->
          val coroutineScope = rememberCoroutineScope()
          val listState = rememberLazyListState()

          val refreshing by remember { viewModel.refreshing }
          val ptrState = rememberPullRefreshState(refreshing, ::refresh)
          val rotation = animateFloatAsState(ptrState.progress * 120)

          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues)
              .pullRefresh(state = ptrState)
          ) {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally,
              state = listState
            ) {
              item {
                Text(
                  text = "Row View",
                  modifier = Modifier.padding(8.dp),
                  style = MaterialTheme.typography.titleLarge
                )
              }
              item {
                RowItem(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), tag = "row"
                )
              }
              item {
                Text(
                  text = "Grid View",
                  modifier = Modifier.padding(8.dp),
                  style = MaterialTheme.typography.titleLarge
                )
              }
              item {
                GridItem(
                  modifier = Modifier.fillMaxWidth(), tag = "grid"
                )
              }

              item {
                Row(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                  horizontalArrangement = Arrangement.SpaceAround
                ) {
                  ChangeUserButton(onSuccess = {
                    refresh()
                    coroutineScope.launch {
                      listState.animateScrollToItem(0)
                    }
                    viewModel.startRefreshing()
                  })
                }
              }
            }

            Surface(
              modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopCenter)
                .pullRefreshIndicatorTransform(ptrState)
                .rotate(rotation.value),
              shape = RoundedCornerShape(10.dp),
              color = Color.DarkGray,
              tonalElevation = if (ptrState.progress > 0 || refreshing) 20.dp else 0.dp
            ) {
              Box {
                if (refreshing) {
                  CircularProgressIndicator(
                    modifier = Modifier
                      .align(Alignment.Center)
                      .size(25.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  @Composable
  private fun RowItem(
    modifier: Modifier = Modifier, tag: String, categories: List<String> = listOf()
  ) {
    StorytellerRowView(modifier = modifier, tag = tag, controller = storytellerComposeController) {
      delegate = this@JetpackComposeActivity
      cellType = StorytellerListViewCellType.SQUARE
      this.categories = categories
      reloadData()
    }
  }

  @Composable
  private fun GridItem(
    modifier: Modifier = Modifier, tag: String, categories: List<String> = listOf()
  ) {
    StorytellerGridView(modifier = modifier, tag = tag, controller = storytellerComposeController) {
      delegate = this@JetpackComposeActivity
      cellType = StorytellerListViewCellType.ROUND
      this.categories = categories
      reloadData()
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

  /*
  Called when the tenant is configured to request ads from the containing app
  and the SDK requires ad data from the containing app
  For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#ClientAds
   */
  override fun getAdsForList(
    stories: List<ClientStory>, onComplete: (AdResponse) -> Unit, onError: () -> Unit
  ) {
    Log.i("Storyteller Sample", "getAdsForRow: stories $stories")
  }
}


@Composable
fun TopBar() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .background(MaterialTheme.colorScheme.primaryContainer)
      .padding(8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    val context = LocalContext.current

    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
      Text(text = "Storyteller Compose")
      Image(
        modifier = Modifier
          .size(16.dp)
          .align(Alignment.Bottom),
        painter = painterResource(id = R.drawable.ic_storyteller),
        contentDescription = "Storyteller Icon"
      )
      Text(
        modifier = Modifier.align(Alignment.Bottom),
        text = "v${Storyteller.version.substringBefore("-")}",
        style = MaterialTheme.typography.bodySmall
      )
      Image(
        modifier = Modifier
          .size(16.dp)
          .align(Alignment.Bottom),
        painter = painterResource(id = R.drawable.ic_compose),
        contentDescription = "Storyteller Icon"
      )
      Text(
        modifier = Modifier.align(Alignment.Bottom),
        text = "v${Storyteller.composeVersion.substringBefore("-")}",
        style = MaterialTheme.typography.bodySmall
      )
    }
    IconButton(onClick = {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("https://getstoryteller.com")
      context.startActivity(intent)
    }) {
      Image(
        painter = painterResource(id = R.drawable.ic_storyteller),
        contentDescription = "Storyteller Logo"
      )
    }
  }
}

@Composable
fun ChangeUserButton(
  modifier: Modifier = Modifier, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}
) {
  val context = LocalContext.current
  Button(modifier = modifier, onClick = {
    val freshUserId = UUID.randomUUID().toString()
    SampleApp.initializeStoryteller(userId = freshUserId, onSuccess = {
      Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
      context.toast("user id changed to: $freshUserId")
      onSuccess.invoke()
    }, onFailure = {
      Log.e("Storyteller Sample", "initialize failed $it}")
      context.toast("failed to change user id")
      onFailure.invoke()
    })
  }) {
    Text("Change user")
  }
}

fun Context.toast(text: String) {
  Handler(Looper.getMainLooper()).post {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
  }
}

