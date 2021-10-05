package com.example.storytellerSampleAndroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.storyteller.Storyteller
import com.storyteller.domain.*
import com.storyteller.services.Error
import com.storyteller.ui.row.StorytellerRowView
import com.storyteller.ui.row.StorytellerRowViewDelegate
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main), StorytellerRowViewDelegate {

    private lateinit var refreshLayout: SwipeRefreshLayout

    private lateinit var storytellerRowView: StorytellerRowView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enable sdk logging for debug
        //Storyteller.enableLogging = true

        /*
        The delegate is used for the SDK to manage events from a StorytellerRowView instance
        Assign it to the appropriate activity to receive callbacks
        For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#storytellerrowviewdelegate
         */
        storytellerRowView = findViewById<StorytellerRowView>(R.id.channelRowView)
        storytellerRowView.delegate = this

        //setup user
        val userId = UUID.randomUUID().toString()
        /*
        The SDK allows to customize theme for Storyteller.
         */
        Storyteller.theme = UiTheme()
        /*
        The SDK requires initialization before it can be used
        This can be done by using a valid API key
        For more info, see - https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#sdk-initialization
         */
        Storyteller.initialize(
            apiKey = "[API-KEY]",
            preloadRowData = true,
            onSuccess = {
                Log.i("Storyteller Sample", "initialize success $userId")

                /*
                        Authenticate a user by setting details containing an UUID
                        For more info, see - https://docs.getstoryteller.com/documents/android-sdk/Users
                         */
                Storyteller.setUserDetails(UserInput(userId))

                /* Tell the SDK to load the latest data from the API*/
                storytellerRowView.reloadData(onComplete = {
                    handleDeepLink(intent?.data)
                    refreshLayout.isRefreshing = false
                })


            },
            onFailure = { Log.i("Storyteller Sample", "initialize failed, error $it") }
        )

        //setup refresh layout
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout).apply {
            setOnRefreshListener {

                /*
                Tell the SDK to load the latest data from the API
                 */
                storytellerRowView.reloadData({
                    refreshLayout.isRefreshing = false
                })
            }
        }
        //setup change user button
        findViewById<Button>(R.id.changeUserButton).apply {
            setOnClickListener {
                /*
                 If you use login in your app and wish to allow users to logout and log back in as a new user
                 (or proceed as an anonymous user) then when a user logs out you should call setUserDetails
                 again specifying a new externalId. Note that this will reset the local store of which pages the user has viewed.
                 For more info, see - https://docs.getstoryteller.com/documents/android-sdk/Users
                 */
                val freshUserId = UUID.randomUUID().toString()
                Storyteller.setUserDetails(UserInput(freshUserId))

                Toast.makeText(
                    this@MainActivity,
                    "New User with Id: $freshUserId",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
             For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowView#openstory
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
    For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#error-handling
     */
    override fun onStoryDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Log.i(
            "Storyteller Sample",
            "onChannelsDataLoadComplete callback: success $success, error $error, dataCount $dataCount"
        )
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
     For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#analytics
     */
    override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
        Log.i("Storyteller Sample", "onUserActivityOccurred: type $type data $data")
    }

    /*
    Called whenever a tile is visible in the story view
    For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#tile-visibility
     */
    override fun tileBecameVisible(storyIndex: Int) {
        Log.i("Storyteller Sample", "tileBecameVisible: storyIndex $storyIndex")
    }

    /*
    Called when a user swipes up on a page which should direct the user
    to a specific place within the integrating app.
    For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#swiping-up-to-the-integrating-app
     */
    override fun userSwipedUpToApp(swipeUpUrl: String) {
        Log.i("Storyteller Sample", "userSwipedUpToApp: swipeUpUrl $swipeUpUrl")
        // Pass swipeUpUrl from SDK callback to OtherActivity where it can be accessed as an extra string value when it is started
        startActivity(Intent(this, OtherActivity::class.java).apply {
            putExtra("EXTRA_SWIPE_URL", swipeUpUrl)
        })
    }

    /*
    Called when the tenant is configured to request ads from the containing app
    and the SDK requires ad data from the containing app
    For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#client-ads
     */
    override fun getAdsForRow(
        stories: List<ClientStory>,
        onComplete: (AdResponse) -> Unit
    ) {
        Log.i("Storyteller Sample", "getAdsForRow: stories $stories")
    }
}
