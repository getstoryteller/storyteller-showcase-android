package com.getstoryteller.showcaseapp

import android.app.Application
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.getstoryteller.showcaseapp.ui.MainActivity.Companion.API_KEY
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import java.util.UUID

class StoryTellerApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Storyteller.initialize(
      apiKey = API_KEY,
      userInput = UserInput(UUID.randomUUID().toString()),
      onSuccess = {
        Log.i("Storyteller", "Storyteller initialized")
      },
      onFailure = { error ->
        Handler(this.mainLooper).post {
          Toast.makeText(this, "Storyteller error $error", Toast.LENGTH_LONG).show()
        }
      },
    )
  }
}
