package com.example.storytellerSampleAndroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storytellerSampleAndroid.databinding.ActivityMainBinding
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.ui.list.StorytellerListViewDelegate

class MainActivity : AppCompatActivity(R.layout.activity_main),
    StorytellerListViewDelegate {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //make layout stable during system UI changes like hiding/showing status bar
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Storyteller.enableLogging = true

        binding.storytellerRowView.apply {
            // this is just a view delegate - not the global one we are interested int terms of the issue which is set in SampleApp.kt
            delegate = this@MainActivity
            categories = listOf("paolo-banchero")
            reloadData()
        }

        binding.refreshLayout.setOnRefreshListener {
            binding.storytellerRowView.reloadData()
        }

        openDeepLink(intent)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        // This method allows smooth close transition syncing. It should be used inside `onActivityReenter` only.
        activityReentered()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        openDeepLink(intent)
    }

    private fun openDeepLink(intent: Intent?) {
        val deepLink = intent?.dataString
        if (deepLink != null && Storyteller.isStorytellerDeepLink(deepLink)) {
            Storyteller.openDeepLink(this, deepLink)
            finish()
        }
    }


    override fun onDataLoadComplete(
        success: Boolean,
        error: com.storyteller.domain.entities.Error?,
        dataCount: Int
    ) {
        Log.i(
            "Storyteller Sample",
            "onDataLoadComplete callback: success $success, error $error, dataCount $dataCount"
        )
        binding.refreshLayout.isRefreshing = false
    }


    override fun onDataLoadStarted() {
    }

    override fun onPlayerDismissed() {
    }

    override fun tileBecameVisible(contentIndex: Int) {
    }
}
