package com.getstoryteller.showcaseapp.ui

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.startup.Initializer
import com.storyteller.Storyteller
import com.storyteller.domain.entities.UserInput
import java.util.UUID

class StorytellerInitializer : Initializer<Unit> {
  override fun create(context: Context): Unit {
    Storyteller.initialize(
      apiKey = "API_KEY",
      userInput = UserInput(UUID.randomUUID().toString()),
      onSuccess = {
        Log.i("Storyteller", "Storyteller initialized")
      },
      onFailure = { error ->
        Handler(context.mainLooper).post {
          Toast.makeText(context, "Storyteller error $error", Toast.LENGTH_LONG).show()
        }
      },
    )
  }
  override fun dependencies(): List<Class<out Initializer<*>>> {
    // No dependencies on other libraries.
    return emptyList()
  }
}
