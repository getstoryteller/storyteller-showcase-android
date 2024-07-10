package com.getstoryteller.storytellershowcaseapp.domain.ports

interface SessionRepository {
  var apiKey: String?
  var userId: String?
  var attributes: Map<String, String?>
  var trackEvents: Boolean
  var allowPersonalization: Boolean
  var allowStoryTellerTracking: Boolean
  var allowUserActivityTracking: Boolean

  fun reset()
}
