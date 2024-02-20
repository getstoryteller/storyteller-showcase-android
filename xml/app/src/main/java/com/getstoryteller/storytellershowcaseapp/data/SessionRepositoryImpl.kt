package com.getstoryteller.storytellershowcaseapp.data

import android.content.SharedPreferences
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import java.util.UUID
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val prefs: SharedPreferences) :
  SessionRepository {
  companion object {
    private const val KEY_APIKEY = "KEY_APIKEY"
    private const val KEY_USER_ID = "KEY_USER_ID"
    private const val KEY_LANGUAGE = "KEY_LANGUAGE"
    private const val KEY_TEAM = "KEY_TEAM"
    private const val KEY_HAS_ACCOUNT = "KEY_HAS_ACCOUNT"
    private const val KEY_TRACK_EVENTS = "KEY_TRACK_EVENTS"
    private const val KEY_CATEGORIES = "KEY_CATEGORIES"
    private const val KEY_COLLECTION = "KEY_COLLECTION"
  }

  override var apiKey: String?
    get() = prefs.getString(KEY_APIKEY, null)
    set(value) = prefs.edit().putString(KEY_APIKEY, value).apply()

  override var userId: String?
    get() = prefs.getString(KEY_USER_ID, null)
    set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

  override var language: String?
    get() = prefs.getString(KEY_LANGUAGE, null)
    set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

  override var team: String?
    get() = prefs.getString(KEY_TEAM, null)
    set(value) = prefs.edit().putString(KEY_TEAM, value).apply()

  override var hasAccount: Boolean
    get() = prefs.getBoolean(KEY_HAS_ACCOUNT, false)
    set(value) = prefs.edit().putBoolean(KEY_HAS_ACCOUNT, value).apply()

  override var trackEvents: Boolean
    get() = prefs.getBoolean(KEY_TRACK_EVENTS, true)
    set(value) = prefs.edit().putBoolean(KEY_TRACK_EVENTS, value).apply()

  override var categories: List<String>
    get() = prefs.getStringSet(KEY_CATEGORIES, emptySet()).orEmpty().toList()
    set(value) = prefs.edit().putStringSet(KEY_CATEGORIES, value.toSet()).apply()

  override var collection: String
    get() = prefs.getString(KEY_COLLECTION, "").orEmpty()
    set(value) = prefs.edit().putString(KEY_COLLECTION, value).apply()

  override fun reset() {
    userId = UUID.randomUUID().toString()
    trackEvents = true
    hasAccount = false
    language = null
    team = null
  }
}
