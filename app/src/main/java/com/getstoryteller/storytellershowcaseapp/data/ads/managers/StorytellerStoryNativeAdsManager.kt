package com.getstoryteller.storytellershowcaseapp.data.ads.managers

import com.getstoryteller.storytellershowcaseapp.data.ads.managers.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.data.ads.kvps.StoriesKVPsProvider
import com.getstoryteller.storytellershowcaseapp.data.ads.mapper.StorytellerAdsMapper
import com.getstoryteller.storytellershowcaseapp.data.ads.tracking.StorytellerAdsTracker
import com.getstoryteller.storytellershowcaseapp.data.ads.entity.StorytellerGoogleAdInfo
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
