package com.example.storytellerSampleAndroid

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.storyteller.Storyteller
import com.storyteller.domain.*
import com.storyteller.services.Error
import com.storyteller.ui.row.StorytellerRowView
import com.storyteller.ui.row.StorytellerRowViewDelegate
import java.util.*

class MainActivity : AppCompatActivity(), StorytellerRowViewDelegate {

    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //enable sdk logging for debug
        //Storyteller.enableLogging = true

        /*
        The delegate is used for the SDK to manage events from a StorytellerRowView instance
        Assign it to the appropriate activity to receive callbacks
        For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#storytellerrowviewdelegate
         */
        val storytellerRowView = findViewById<StorytellerRowView>(R.id.channelRowView)
        storytellerRowView.delegate = this

        //setup user
        val userId = UUID.randomUUID().toString()

        /*
        The SDK requires initialization before it can be used
        This can be done by using a valid API key
        For more info, see - https://docs.getstoryteller.com/documents/android-sdk/GettingStarted#sdk-initialization
         */
        Storyteller.initialize("[API KEY]", {
            Log.i("Storyteller Sample", "initialize success $userId")

            /*
            Authenticate a user by setting details containing an UUID
            For more info, see - https://docs.getstoryteller.com/documents/android-sdk/Users
             */
            Storyteller.setUserDetails(UserInput(userId))

            /*
            Tell the SDK to load the latest data from the API
             */
            storytellerRowView.reloadData({
                handleDeepLink(intent?.data)
                refreshLayout.isRefreshing = false
            })


        },{ Log.i("Storyteller Sample", "initialize failed, error $it") })

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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent?.data)
    }

    /*
    Called when the data loading network request is complete
    For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#error-handling
     */
    override fun onStoryDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Log.i("Storyteller Sample", "onChannelsDataLoadComplete callback: success $success, error $error, dataCount $dataCount")
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
    }

    /*
    Called when the tenant is configured to request ads from the containing app
    and the SDK requires ad data from the containing app
    For more info, see - https://docs.getstoryteller.com/documents/android-sdk/StorytellerRowViewDelegate#client-ads
     */
    override fun getAdsForRow(stories: List<ClientStory>, onComplete: (AdResponse) -> Unit) {
        Log.i("Storyteller Sample", "getAdsForRow: stories $stories")
    }

    private fun handleDeepLink(data: Uri?) {
        if (data != null) {
            val pageId = data.lastPathSegment
            findViewById<StorytellerRowView>(R.id.channelRowView).openPage(
                pageId
            )  { Log.e("DEBUG", "Cannot open deep link $data", it) }
        }
    }
}
