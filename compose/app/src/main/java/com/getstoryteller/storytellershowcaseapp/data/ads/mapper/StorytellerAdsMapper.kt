package com.getstoryteller.storytellershowcaseapp.data.ads.mapper

import com.getstoryteller.storytellershowcaseapp.data.ads.entity.AdConstants
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.domain.entities.ads.StorytellerAdAction
import com.storyteller.remote.ads.StorytellerAdTrackingPixel
import javax.inject.Inject

class StorytellerAdsMapper @Inject constructor() {
  fun map(
    ad: NativeCustomFormatAd,
    adKey: String,
  ): StorytellerAd? {
    val advertiserName = ad.getText(AdConstants.ADVERTISER_NAME)?.toString()
    val creativeType = ad.getText(AdConstants.CREATIVE_TYPE)?.toString()
    val image = ad.getImage(AdConstants.IMAGE)?.uri?.toString()
    val video = ad.getText(AdConstants.VIDEO_URL)?.toString()
    val clickType = ad.getText(AdConstants.CLICK_TYPE)?.toString()
    val clickThroughUrl = ad.getText(AdConstants.CLICK_URL)?.toString()
    val playStoreId = ad.getText(AdConstants.PLAY_STORE_ID)?.toString()
    val clickThroughCTA = ad.getText(AdConstants.CLICK_CTA)?.toString()

    val trackingUrl = ad.getText(AdConstants.TRACKING_URL)?.toString()
    val trackingPixels = mutableListOf<StorytellerAdTrackingPixel>()
    if (trackingUrl != null) {
      trackingPixels.add(
        StorytellerAdTrackingPixel(
          eventType = UserActivity.EventType.OPENED_AD,
          url = trackingUrl,
        ),
      )
    }

    return if (creativeType == AdConstants.DISPLAY && image != null) {
      StorytellerAd.createImageAd(
        id = adKey,
        advertiserName = advertiserName,
        image = image,
        storytellerAdAction =
        if (!clickType.isNullOrEmpty()) {
          createAdAction(
            clickType = clickType,
            clickThroughUrl = clickThroughUrl,
            clickCTA = clickThroughCTA,
            playStoreId = playStoreId,
          )
        } else {
          null
        },
        trackingPixels = trackingPixels,
      )
    } else if (creativeType == AdConstants.VIDEO && video != null) {
      StorytellerAd.createVideoAd(
        id = adKey,
        advertiserName = advertiserName,
        video = video,
        storytellerAdAction =
        if (!clickType.isNullOrEmpty()) {
          createAdAction(
            clickType = clickType,
            clickThroughUrl = clickThroughUrl,
            clickCTA = clickThroughCTA,
            playStoreId = playStoreId,
          )
        } else {
          null
        },
        trackingPixels = trackingPixels,
      )
    } else {
      null
    }
  }

  private fun createAdAction(
    clickType: String,
    clickThroughUrl: String?,
    clickCTA: String?,
    playStoreId: String?,
  ): StorytellerAdAction? =
    when {
      clickType.equals(
        AdConstants.WEB,
        ignoreCase = true,
      ) && clickThroughUrl != null -> StorytellerAdAction.createWebAction(clickThroughUrl, clickCTA)

      clickType.equals(
        AdConstants.IN_APP,
        ignoreCase = true,
      ) && clickThroughUrl != null -> StorytellerAdAction.createInAppAction(clickThroughUrl, clickCTA)

      clickType.equals(
        AdConstants.STORE,
        ignoreCase = true,
      ) && playStoreId != null -> StorytellerAdAction.createStoreAction(playStoreId, clickCTA)

      else -> null
    }
}
