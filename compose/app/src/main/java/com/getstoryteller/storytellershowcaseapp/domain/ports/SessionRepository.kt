package com.getstoryteller.storytellershowcaseapp.domain.ports

interface SessionRepository {
  var apiKey: String?
  var userId: String?
  var language: String?
  var team: String?
  var hasAccount: Boolean
  var trackEvents: Boolean
  var allowPersonalization: Boolean
  var allowStoryTellerTracking: Boolean
  var allowUserActivityTracking: Boolean

  fun reset()
}
