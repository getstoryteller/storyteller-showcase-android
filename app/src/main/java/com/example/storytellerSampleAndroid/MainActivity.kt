package com.example.storytellerSampleAndroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.storyteller.Storyteller
import com.storyteller.domain.UserInput
import com.storyteller.services.Error
import com.storyteller.ui.row.StorytellerRowView
import com.storyteller.ui.row.StorytellerRowViewDelegate
import java.util.*

class MainActivity : AppCompatActivity(), StorytellerRowViewDelegate {

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
        storytellerRowView.delegate = this

        Storyteller.initialize("[APIKEY]")
    }

    override fun onChannelDismissed() {
        Log.i("Storyteller Sample", "onChannelDismissed callback")

    }

    override fun onChannelsDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Log.i("Storyteller Sample", "onChannelsDataLoadComplete callback: success $success, error $error, dataCount $dataCount")
    }

    override fun onChannelsDataLoadStarted() {
        Log.i("Storyteller Sample", "onChannelsDataLoadStarted callback")
    }

}
