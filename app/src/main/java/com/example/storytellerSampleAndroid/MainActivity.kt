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

        //set callbacks
        val storytellerRowView = findViewById<StorytellerRowView>(R.id.channelRowView)
        storytellerRowView.delegate = this

        //setup user
        val userId = UUID.randomUUID().toString()
        //initialize sdk
        Storyteller.initialize("[API KEY]]", {
            Log.i("Storyteller Sample", "initialize success $userId")
            Storyteller.setUserDetails(UserInput(userId))
            storytellerRowView.reloadData({
                handleDeepLink(intent?.data)
                refreshLayout.isRefreshing = false
            })
        },{ Log.i("Storyteller Sample", "initialize failed, error $it") })

        //setup refresh layout
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout).apply {
            setOnRefreshListener {
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

    override fun onStoryDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Log.i("Storyteller Sample", "onChannelsDataLoadComplete callback: success $success, error $error, dataCount $dataCount")
    }

    override fun onStoryDataLoadStarted() {
        Log.i("Storyteller Sample", "onStoryDataLoadStarted callback")
    }

    override fun onStoryDismissed() {
        Log.i("Storyteller Sample", "onStoryDismissed callback")
    }

    override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
        Log.i("Storyteller Sample", "onUserActivityOccurred: type $type data $data")
    }

    override fun tileBecameVisible(storyIndex: Int) {
        Log.i("Storyteller Sample", "tileBecameVisible: storyIndex $storyIndex")
    }

    override fun userSwipedUpToApp(swipeUpUrl: String) {
        Log.i("Storyteller Sample", "userSwipedUpToApp: swipeUpUrl $swipeUpUrl")
    }

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
