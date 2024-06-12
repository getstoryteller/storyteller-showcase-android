package com.getstoryteller.storytellershowcaseapp.ui.features

import android.app.Activity
import androidx.core.net.toUri
import com.storyteller.Storyteller

class DeeplinkHandler {
  companion object {
    fun handleDeepLink(
      activity: Activity,
      deeplink: String,
      onNavigate: (String) -> Unit,
    ) {
      val uri = deeplink.toUri()

      when (uri.host) {
        "moments" -> {
          val clipId = uri.pathSegments.firstOrNull()
          onNavigate("home/moments?clipId=$clipId")
          return
        }
      }
      if (Storyteller.isStorytellerDeepLink(deeplink)) {
        Storyteller.openDeepLink(activity, deeplink)
      }
    }
  }
}
