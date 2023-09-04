package com.example.storytellerSampleAndroid

import android.app.Application
import android.util.Log
import com.example.storytellerSampleAndroid.preferences.SharedPreferencesManager
import com.example.storytellerSampleAndroid.theme.StorytellerThemes
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import com.storyteller.domain.entities.Error
import java.util.UUID

class SampleApp : Application() {

  companion object {
    fun initializeStoryteller(
      userId: String?,
      onSuccess: () -> Unit = {},
      onFailure: (Error) -> Unit = {}
    ) {
      /*
         The SDK requires initialization before it can be used
         This can be done by using a valid API key
         For more info, see - https://www.getstoryteller.com/documentation/android/getting-started#SDKInitialization
          */

      val user = if (userId == null) {
        null
      } else {
        UserInput(userId)
      }
      Storyteller.initialize(
        // change to your own [API-KEY]
        apiKey = "02e18252-5b39-40fe-8ddc-813c864bc663",
        userInput = user,
        onSuccess = onSuccess,
        onFailure = onFailure
      )
    }
  }

  private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

  override fun onCreate() {
    super.onCreate()
    /*
   The SDK allows to customize theme for Storyteller.
   Make sure that global theme is initialized before views are being inflated or created.
   */
    Storyteller.theme = StorytellerThemes.getGlobalTheme(this)
    initializeStoryteller(
      userId = sharedPreferencesManager.userId,
      onSuccess = {
        Storyteller.user.setCustomAttribute("favoriteTeam", sharedPreferencesManager.team)
        Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
      }, onFailure = { error ->
        Log.i("Storyteller Sample", "initialize failed, error $error")
      })
  }
}