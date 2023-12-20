package com.getstoryteller.storytellershowcaseapp.ads.managers

import com.getstoryteller.storytellershowcaseapp.ads.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.ads.kvps.StoriesKVPsProvider
import com.getstoryteller.storytellershowcaseapp.ads.mapper.StorytellerAdsMapper
import com.getstoryteller.storytellershowcaseapp.ads.tracking.StorytellerAdsTracker
import com.getstoryteller.storytellershowcaseapp.ads.provider.google.StorytellerGoogleAdInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorytellerStoryAdsManager @Inject constructor(
  googleNativeAdsManager: GoogleNativeAdsManager,
  storytellerAdsMapper: StorytellerAdsMapper,
  storiesKVPsProvider: StoriesKVPsProvider,
  storytellerAdsTracker: StorytellerAdsTracker
) : StorytellerAdsManager<StorytellerAdRequestInfo.StoriesAdRequestInfo>(
  storytellerKVPProvider = storiesKVPsProvider,
  googleNativeAdsManager = googleNativeAdsManager,
  googleAdInfo = StorytellerGoogleAdInfo.STORY_ADS,
  storytellerAdsMapper = storytellerAdsMapper,
  storytellerAdsTracker = storytellerAdsTracker
)
