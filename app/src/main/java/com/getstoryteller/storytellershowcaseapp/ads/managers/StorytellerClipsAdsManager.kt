package com.getstoryteller.storytellershowcaseapp.ads.managers

import com.getstoryteller.storytellershowcaseapp.ads.kvps.ClipsKVPsProvider
import com.getstoryteller.storytellershowcaseapp.ads.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.ads.mapper.StorytellerAdsMapper
import com.getstoryteller.storytellershowcaseapp.ads.tracking.StorytellerAdsTracker
import com.getstoryteller.storytellershowcaseapp.ads.provider.google.StorytellerGoogleAdInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorytellerClipsAdsManager @Inject constructor(
  googleNativeAdsManager: GoogleNativeAdsManager,
  storytellerAdsMapper: StorytellerAdsMapper,
  clipsKVPsProvider: ClipsKVPsProvider,
  storytellerAdsTracker: StorytellerAdsTracker
) : StorytellerAdsManager<StorytellerAdRequestInfo.ClipsAdRequestInfo>(
  storytellerKVPProvider = clipsKVPsProvider,
  googleNativeAdsManager = googleNativeAdsManager,
  googleAdInfo = StorytellerGoogleAdInfo.CLIP_ADS,
  storytellerAdsMapper = storytellerAdsMapper,
  storytellerAdsTracker = storytellerAdsTracker
)
