package com.getstoryteller.storytellershowcaseapp.ads.entity

data class StorytellerGoogleAdInfo(
  val adUnitId: String,
  val templateId: String,
) {
  companion object {
    val STORY_ADS = StorytellerGoogleAdInfo(
      adUnitId = "/33813572/stories-native-ad-unit",
      templateId = "12102683"
    )
    val CLIP_ADS = StorytellerGoogleAdInfo(
      adUnitId = "/33813572/clips-native-ad-unit",
      templateId = "12269089"
    )
  }
}
