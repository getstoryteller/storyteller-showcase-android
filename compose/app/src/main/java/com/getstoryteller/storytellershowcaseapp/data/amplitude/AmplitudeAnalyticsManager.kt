package com.getstoryteller.storytellershowcaseapp.data.amplitude

import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmplitudeAnalyticsManager @Inject constructor(
  private val amplitudeService: AmplitudeService,
) {
  fun handleAnalyticsEvents(
    type: UserActivity.EventType,
    data: UserActivityData,
    location: String?,
  ) {
    when (type) {
      UserActivity.EventType.OPENED_CLIP,
      UserActivity.EventType.OPENED_STORY,
      -> logAmplitudeEvent(type, data, location)
      else -> Unit
    }
  }

  private fun logAmplitudeEvent(
    eventType: UserActivity.EventType,
    data: UserActivityData,
    location: String?,
  ) {
    val eventData =
      mapOf(
        "Story ID" to data.storyId,
        "Story Category" to data.categories?.joinToString(separator = ";"),
        "Page ID" to data.pageId,
        "Story Title" to data.storyTitle,
        "Page Index" to data.pageIndex,
        "Clip ID" to data.clipId,
        // assuming clipIndex exists in your data model
        "Clip Index" to data.clipIndex,
        "Clip Title" to data.clipTitle,
        "Clip Collection" to data.collection,
        "Location" to location,
      )

    amplitudeService.track(eventType.name, eventData)
    Timber.d("Amplitude event logged: $eventType with data: $eventData")
  }
}
