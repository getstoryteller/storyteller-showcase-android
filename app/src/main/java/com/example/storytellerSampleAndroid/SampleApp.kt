package com.example.storytellerSampleAndroid

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import com.storyteller.domain.entities.theme.builders.buildTheme
import com.storyteller.domain.entities.Error
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.ads.AdResponse
import com.storyteller.domain.entities.ads.ClientStory
import com.storyteller.domain.entities.ads.ListDescriptor
import com.storyteller.ui.list.StorytellerDelegate
import java.util.UUID

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Storyteller.storytellerDelegate = storytellerDelegate
        Storyteller.initialize(
            // change to your own [API-KEY]
            apiKey = "b69b1ffe-b2fa-436a-ad75-108806e795a6",
            userInput = UserInput(UUID.randomUUID().toString()),
            onFailure = { error ->
                Log.e("Storyteller Sample", "Failed to initialize Storyteller: $error")
            },
            onSuccess = {
                Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
            }
        )

    }

    private val storytellerDelegate = object :StorytellerDelegate{

        override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
            Log.i("Storyteller Sample", "onUserActivityOccurred $type")
        }

        override fun userNavigatedToApp(url: String) {
            Log.i("Storyteller Sample", "userNavigatedToApp url = $url")
        }

        override fun configureWebView(view: WebView, url: String?, favicon: Bitmap?) = Unit

        override fun getAdsForList(
            listDescriptor: ListDescriptor,
            stories: List<ClientStory>,
            onComplete: (AdResponse) -> Unit,
            onError: () -> Unit
        ) = Unit

        override fun getAdsForList(
            stories: List<ClientStory>,
            onComplete: (AdResponse) -> Unit,
            onError: () -> Unit
        ) = Unit

    }
}