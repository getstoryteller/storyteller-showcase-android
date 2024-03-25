package com.example.storytellerSampleAndroid

import android.app.Application
import android.util.Log
import com.example.storytellerSampleAndroid.theme.StorytellerThemes
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import com.storyteller.domain.entities.Error
import java.util.UUID

class SampleApp : Application() {

    companion object {
        fun initializeStoryteller(
            userId: String = UUID.randomUUID().toString(),
            onSuccess: () -> Unit = {},
            onFailure: (Error) -> Unit = {}
        ) {
            /*
               The SDK requires initialization before it can be used
               This can be done by using a valid API key
               For more info, see - https://www.getstoryteller.com/documentation/android/getting-started#SDKInitialization
                */
            Storyteller.initialize(
                // change to your own [API-KEY]
                apiKey = "6a0ea73b-7b5d-42ab-bbf9-0584a696d9bb",
                userInput = UserInput(userId),
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        /*
       The SDK allows to customize theme for Storyteller.
       Make sure that global theme is initialized before views are being inflated or created.
       */
        Storyteller.theme = StorytellerThemes.getGlobalTheme(this)


        initializeStoryteller(onSuccess = {
            Storyteller.user.setCustomAttribute("name", "John Doe")
            Storyteller.user.setCustomAttribute("name2", "John Doe2")
            Storyteller.user.setCustomAttribute("name3", "John Doe3")
            Storyteller.user.setCustomAttribute("name4", "John Doe4")
            Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUserId}")
        }, onFailure = { error ->
            Log.i("Storyteller Sample", "initialize failed, error $error")
        })

    }
}