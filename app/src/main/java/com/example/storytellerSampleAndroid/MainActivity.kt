package com.example.storytellerSampleAndroid

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.storyteller.Storyteller
import com.storyteller.domain.UserInput
import com.storyteller.services.Error
import com.storyteller.ui.row.StoryRowCallbacks
import com.storyteller.ui.row.StorytellerRowView
import java.util.*

class MainActivity : AppCompatActivity(), StoryRowCallbacks {

    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup user
        val userId = UUID.randomUUID().toString()
        Storyteller.setUserDetails(UserInput(userId), {
            Log.i("Storyteller Sample", "setUserDetails success $userId")
            Storyteller.reloadData({
                refreshLayout.isRefreshing = false
            })
        }, {
            Log.i("Storyteller Sample", "setUserDetails failed, error $it")
        })

        //setup refresh layout
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout).apply {
            setOnRefreshListener {
                Storyteller.reloadData({
                    refreshLayout.isRefreshing = false
                })
            }
        }

        //setup callbacks
        val storytellerRowView = findViewById<StorytellerRowView>(R.id.channelRowView)
        storytellerRowView.storyRowCallbacks = this

        Storyteller.setErrorCallback {
            Log.i("Storyteller Sample", "setErrorCallback, error $it")

        }
    }

    override fun onStoriesLoadFailure(error: Error) {
        Log.i("Storyteller Sample", "onStoriesLoadFailure $error")

    }
}
