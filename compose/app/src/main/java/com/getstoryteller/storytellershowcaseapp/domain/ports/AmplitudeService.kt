package com.getstoryteller.storytellershowcaseapp.domain.ports

interface AmplitudeService {
  fun init()

  fun logout()

  fun track(
    event: String,
    data: Map<String, Any?>,
  )
}
