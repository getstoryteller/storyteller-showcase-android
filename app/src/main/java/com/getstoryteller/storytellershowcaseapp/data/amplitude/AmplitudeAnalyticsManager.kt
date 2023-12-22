package com.getstoryteller.storytellershowcaseapp.data.amplitude

import com.amplitude.analytics.connector.util.toJSONObject
import com.amplitude.api.Amplitude
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmplitudeAnalyticsManager @Inject constructor() {

  fun handleAnalyticsEvents(type: UserActivity.EventType, data: UserActivityData) {
    when(type){
      UserActivity.EventType.OPENED_CLIP,
      UserActivity.EventType.OPENED_STORY -> logAmplitudeEvent(type, data)
      else -> Unit
    }
  }

  private fun logAmplitudeEvent(eventType: UserActivity.EventType, data: UserActivityData) {
    val eventData = mapOf(
      "Story ID" to data.storyId,
      "Story Category" to data.categories?.joinToString(separator = ";"),
      "Page ID" to data.pageId,
      "Story Title" to data.storyTitle,
      "Page Index" to data.pageIndex,
      "Clip ID" to data.clipId,
      "Clip Index" to data.clipIndex, // assuming clipIndex exists in your data model
      "Clip Title" to data.clipTitle,
      "Clip Collection" to data.collection
    )

    Amplitude.getInstance().logEvent(eventType.name, eventData.toJSONObject())
    Timber.d("Amplitude event logged: $eventType with data: $eventData")
  }
}
