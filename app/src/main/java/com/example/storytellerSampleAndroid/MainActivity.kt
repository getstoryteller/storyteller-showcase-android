package com.example.storytellerSampleAndroid

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.storyteller.Storyteller
import com.storyteller.domain.*
import com.storyteller.services.Error
import com.storyteller.ui.list.StorytellerDelegate
import com.storyteller.ui.list.StorytellerListViewDelegate
import com.storyteller.ui.list.StorytellerRowView
import com.storyteller.ui.list.StorytellerGridView

import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main), StorytellerDelegate,
    StorytellerListViewDelegate {

    private lateinit var refreshLayout: SwipeRefreshLayout

    private lateinit var storytellerRowView: StorytellerRowView

    private lateinit var storytellerGridView: StorytellerGridView

    init {
        /*
         The SDK allows to customize theme for Storyteller.
         Make sure that global theme is initialized before views are being inflated or created.
         */
        Storyteller.theme = UiTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //make layout stable during system UI changes like hiding/showing status bar
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //enable sdk logging for debug
        //Storyteller.enableLogging = true

        /*
        The global delegate is used for data related events coming from the StoryPlayer
        */
        Storyteller.storytellerDelegate = this
        /*
        The delegate is used for the SDK to manage events from a StorytellerRowView instance
        Assign it to the appropriate activity to receive callbacks
        For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#HowToUse
         */
        storytellerRowView = findViewById(R.id.channelRowView)
        storytellerGridView = findViewById(R.id.channelGridView)

        storytellerRowView.delegate = this
        storytellerGridView.delegate = this
        //setup user
        val userId = UUID.randomUUID().toString()

        //setup refresh layout
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout).apply {
            setOnRefreshListener {
                /*
                Tell the SDK to load the latest data from the API
                 */
                storytellerRowView.reloadData()
                storytellerGridView.reloadData()
            }
        }

        //initialize Storyteller!!!
        initializeStoryteller(userId)

        //setup change user button
        findViewById<Button>(R.id.changeUserButton).apply {
            setOnClickListener {
                /*
                 If you use login in your app and wish to allow users to logout and log back in as a new user
                 (or proceed as an anonymous user) then when a user logs out you should call initialize
                 again specifying a new externalId. Note that this will reset the local store of which pages the user has viewed.
                 For more info, see - https://www.getstoryteller.com/documentation/android/users
                 */
                val freshUserId = UUID.randomUUID().toString()
                initializeStoryteller(freshUserId)
                Toast.makeText(
                    this@MainActivity,
                    "New User with Id: $freshUserId",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun initializeStoryteller(userId: String) {
        /*
           The SDK requires initialization before it can be used
           This can be done by using a valid API key
           For more info, see - https://www.getstoryteller.com/documentation/android/getting-started#SDKInitialization
            */
        Storyteller.initialize(
            apiKey = "[API KEY]",
            userInput = UserInput(userId),
            onSuccess = {
                Log.i("Storyteller Sample", "initialize success $userId")
                /* Tell the SDK to load the latest data from the API*/
                storytellerRowView.reloadData()
            },
            onFailure = { Log.i("Storyteller Sample", "initialize failed, error $it") }
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent?.data)
    }

    private fun handleDeepLink(data: Uri?) {
        if (data != null) {
            /*
             If your app needs to open specific story or page e.g. when opening an activity from a deep link,
             then you should call openStory(storyId) or openPage(pageId). It can be tested in the Sample App with adb command:
             adb shell am start -W -a android.intent.action.VIEW -d "https://storytellersampleapp/[PAGE_ID]"
             For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view#Methods
             */
            val pageId = data.lastPathSegment
            storytellerRowView.openPage(pageId) {
                Log.e(
                    "Storyteller Sample",
                    "Cannot open deep link $data",
                    it
                )
            }
        }
    }

    /*
    Called when the data loading network request is complete
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-list-view-delegate#ErrorHandling
     */
    override fun onStoryDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Log.i(
            "Storyteller Sample",
            "onChannelsDataLoadComplete callback: success $success, error $error, dataCount $dataCount"
        )
        refreshLayout.isRefreshing = false
        if (success) {
            handleDeepLink(intent?.data)
        }
    }

    /*
    Called when the network request to load data for all stories has started
     */
    override fun onStoryDataLoadStarted() {
        Log.i("Storyteller Sample", "onStoryDataLoadStarted callback")
    }

    /*
    Called when any story has been dismissed
     */
    override fun onStoryDismissed() {
        Log.i("Storyteller Sample", "onStoryDismissed callback")
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
    override fun tileBecameVisible(storyIndex: Int) {
        Log.i("Storyteller Sample", "tileBecameVisible: storyIndex $storyIndex")
    }

    /*
    Called when a user swipes up on a page which should direct the user
    to a specific place within the integrating app.
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#SwipingUpToTheIntegratingApp
     */
    override fun userSwipedUpToApp(swipeUpUrl: String) {
        Log.i("Storyteller Sample", "userSwipedUpToApp: swipeUpUrl $swipeUpUrl")
        // Pass swipeUpUrl from SDK callback to OtherActivity where it can be accessed as an extra string value when it is started
        startActivity(Intent(this, OtherActivity::class.java).apply {
            putExtra("EXTRA_SWIPE_URL", swipeUpUrl)
        })
    }

    /*
   Called when a user swipes up on a page which opens a web link.
   Allows to configure WebViewClient if required.
   For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#HowToUse
    */
    override fun configureSwipeUpWebView(view: WebView, url: String?, favicon: Bitmap?) {
        Log.i("Storyteller Sample", "configureSwipeUpWebView $url")
    }

    /*
    Called when the tenant is configured to request ads from the containing app
    and the SDK requires ad data from the containing app
    For more info, see - https://www.getstoryteller.com/documentation/android/storyteller-delegate#ClientAds
     */
    override fun getAdsForList(
        stories: List<ClientStory>,
        onComplete: (AdResponse) -> Unit
    ) {
        Log.i("Storyteller Sample", "getAdsForRow: stories $stories")
    }
}
