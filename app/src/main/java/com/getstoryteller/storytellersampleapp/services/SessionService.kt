package com.getstoryteller.storytellersampleapp.services

import android.content.SharedPreferences

interface SessionService {
    var apiKey: String?
    var userId: String?
    var language: String?
    var team: String?
    var hasAccount: Boolean
}

class SessionServiceImpl(private val prefs: SharedPreferences) : SessionService {

    companion object {
        private const val KEY_APIKEY = "KEY_APIKEY"
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_LANGUAGE = "KEY_LANGUAGE"
        private const val KEY_TEAM = "KEY_TEAM"
        private const val KEY_HAS_ACCOUNT = "KEY_HAS_ACCOUNT"
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
}