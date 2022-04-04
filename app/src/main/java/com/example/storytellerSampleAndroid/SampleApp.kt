package com.example.storytellerSampleAndroid

import android.app.Application
import android.util.Log
import com.storyteller.Storyteller
import com.storyteller.domain.UserInput
import com.storyteller.domain.theme.builders.buildTheme
import com.storyteller.domain.theme.builders.from
import com.storyteller.services.Error
import java.util.*

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
                apiKey = "[APIKEY]",
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
        Storyteller.theme = buildTheme(this) {
            /* MODIFY your theme here
             eg.
            light.colors.primary = ofHexCode("#FF00FF")
            dark from light

            */
        }
        initializeStoryteller(
            onSuccess = {
                Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
            },
            onFailure = { error ->
                Log.i("Storyteller Sample", "initialize failed, error $error")
            })

    }
}