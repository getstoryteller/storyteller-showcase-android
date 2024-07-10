package com.getstoryteller.storytellershowcaseapp.data

import android.content.SharedPreferences
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.storyteller.Storyteller
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val prefs: SharedPreferences) :
  SessionRepository {
  companion object {
    private const val KEY_APIKEY = "KEY_APIKEY"
    private const val KEY_USER_ID = "KEY_USER_ID"
    private const val KEY_ATTRIBUTES = "KEY_ATTRIBUTES"
    private const val KEY_TRACK_EVENTS = "KEY_TRACK_EVENTS"
    private const val KEY_ALLOW_PERSONALIZATION = "KEY_ALLOW_PERSONALIZATION"
    private const val KEY_ALLOW_STORYTELLER_TRACKING = "KEY_ALLOW_STORYTELLER_TRACKING"
    private const val KEY_ALLOW_USER_ACTIVITY_TRACKING = "KEY_ALLOW_USER_ACTIVITY_TRACKING"
  }

  private val json = Json { encodeDefaults = true }

  override var apiKey: String?
    get() = prefs.getString(KEY_APIKEY, null)
    set(value) = prefs.edit().putString(KEY_APIKEY, value).apply()

  override var userId: String?
    get() = prefs.getString(KEY_USER_ID, null)
    set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

  override var attributes: Map<String, String?>
    get() {
      val jsonString = prefs.getString(KEY_ATTRIBUTES, null) ?: return emptyMap()
      return json.decodeFromString(MapSerializer(String.serializer(), String.serializer().nullable), jsonString)
    }
    set(value) {
      val jsonString = json.encodeToString(MapSerializer(String.serializer(), String.serializer().nullable), value)
      prefs.edit().putString(KEY_ATTRIBUTES, jsonString).apply()
    }

  override var trackEvents: Boolean
    get() = prefs.getBoolean(KEY_TRACK_EVENTS, true)
    set(value) = prefs.edit().putBoolean(KEY_TRACK_EVENTS, value).apply()

  override var allowPersonalization: Boolean
    get() = prefs.getBoolean(KEY_ALLOW_PERSONALIZATION, true)
    set(value) = prefs.edit().putBoolean(KEY_ALLOW_PERSONALIZATION, value).apply().also {
      setAnalyticsOptions(personalization = value)
    }

  override var allowStoryTellerTracking: Boolean
    get() = prefs.getBoolean(KEY_ALLOW_STORYTELLER_TRACKING, true)
    set(value) = prefs.edit().putBoolean(KEY_ALLOW_STORYTELLER_TRACKING, value).apply().also {
      setAnalyticsOptions(storyTeller = value)
    }

  override var allowUserActivityTracking: Boolean
    get() = prefs.getBoolean(KEY_ALLOW_USER_ACTIVITY_TRACKING, true)
    set(value) = prefs.edit().putBoolean(KEY_ALLOW_USER_ACTIVITY_TRACKING, value).apply().also {
      setAnalyticsOptions(userActivity = value)
    }

  override fun reset() {
    userId = UUID.randomUUID().toString()
    trackEvents = true
    attributes = emptyMap()
    allowPersonalization = true
    allowStoryTellerTracking = true
    allowUserActivityTracking = true
  }

  private fun setAnalyticsOptions(
    personalization: Boolean = Storyteller.eventTrackingOptions.enablePersonalization,
    storyTeller: Boolean = Storyteller.eventTrackingOptions.enableStorytellerTracking,
    userActivity: Boolean = Storyteller.eventTrackingOptions.enableUserActivityTracking,
  ) {
    Storyteller.eventTrackingOptions = Storyteller.StorytellerEventTrackingOptions(
      enablePersonalization = personalization,
      enableStorytellerTracking = storyTeller,
      enableUserActivityTracking = userActivity,
    )
  }
}
