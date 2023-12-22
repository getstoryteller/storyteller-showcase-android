package com.getstoryteller.storytellershowcaseapp.data.ads.entity

import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo

/**
 * Data class to help binds native ad to the story it was requested for.
 */
data class StorytellerNativeAd(
    val entity: StorytellerAdRequestInfo.ItemInfo, val nativeAd: NativeCustomFormatAd?
)
