package com.getstoryteller.storytellershowcaseapp.ads.managers

import com.getstoryteller.storytellershowcaseapp.ads.managers.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.ads.kvps.StoriesKVPsProvider
import com.getstoryteller.storytellershowcaseapp.ads.mapper.StorytellerAdsMapper
import com.getstoryteller.storytellershowcaseapp.ads.tracking.StorytellerAdsTracker
import com.getstoryteller.storytellershowcaseapp.ads.entity.StorytellerGoogleAdInfo
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorytellerStoryNativeAdsManager @Inject constructor(
  googleNativeAdsManager: GoogleNativeAdsManager,
  storytellerAdsMapper: StorytellerAdsMapper,
  storiesKVPsProvider: StoriesKVPsProvider,
  storytellerAdsTracker: StorytellerAdsTracker
) : StorytellerNativeAdsManager<StorytellerAdRequestInfo.StoriesAdRequestInfo>(
  storytellerKVPProvider = storiesKVPsProvider,
  googleNativeAdsManager = googleNativeAdsManager,
  googleAdInfo = StorytellerGoogleAdInfo.STORY_ADS,
  storytellerAdsMapper = storytellerAdsMapper,
  storytellerAdsTracker = storytellerAdsTracker
)
