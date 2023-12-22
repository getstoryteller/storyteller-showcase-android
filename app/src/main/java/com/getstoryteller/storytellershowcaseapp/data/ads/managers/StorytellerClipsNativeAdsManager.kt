package com.getstoryteller.storytellershowcaseapp.data.ads.managers

import com.getstoryteller.storytellershowcaseapp.data.ads.kvps.ClipsKVPsProvider
import com.getstoryteller.storytellershowcaseapp.data.ads.managers.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.data.ads.mapper.StorytellerAdsMapper
import com.getstoryteller.storytellershowcaseapp.data.ads.tracking.StorytellerAdsTracker
import com.getstoryteller.storytellershowcaseapp.data.ads.entity.StorytellerGoogleAdInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorytellerClipsNativeAdsManager @Inject constructor(
  googleNativeAdsManager: GoogleNativeAdsManager,
  storytellerAdsMapper: StorytellerAdsMapper,
  clipsKVPsProvider: ClipsKVPsProvider,
  storytellerAdsTracker: StorytellerAdsTracker
) : StorytellerNativeAdsManager<StorytellerAdRequestInfo.ClipsAdRequestInfo>(
  storytellerKVPProvider = clipsKVPsProvider,
  googleNativeAdsManager = googleNativeAdsManager,
  googleAdInfo = StorytellerGoogleAdInfo.CLIP_ADS,
  storytellerAdsMapper = storytellerAdsMapper,
  storytellerAdsTracker = storytellerAdsTracker
)
