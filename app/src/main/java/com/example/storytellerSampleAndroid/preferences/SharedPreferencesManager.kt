package com.example.storytellerSampleAndroid.preferences

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {

  private val sharedPrefs = context.getSharedPreferences("StorytellerSampleSharedPrefs", Context.MODE_PRIVATE)

  var userId: String?
    get() = sharedPrefs.getString("UserID", "")
    set(value) = sharedPrefs.edit { putString("UserID", value) }

  var language: String
    get() = sharedPrefs.getString("Lang", "EN") ?: "EN"
    set(value) = sharedPrefs.edit { putString("Lang", value) }

  var team: String
    get() = sharedPrefs.getString("Team", "None") ?: "None"
    set(value) = sharedPrefs.edit { putString("Team", value) }
}