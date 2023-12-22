package com.getstoryteller.storytellershowcaseapp.data.ads.kvps

import com.storyteller.Storyteller
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import javax.inject.Inject

class StoriesKVPsProvider @Inject constructor() :
  StorytellerKVPProvider<StorytellerAdRequestInfo.StoriesAdRequestInfo> {
  override fun getKVPs(adRequestInfo: StorytellerAdRequestInfo.StoriesAdRequestInfo) = mapOf(
    "stStoryId" to adRequestInfo.itemInfo.id,
    "stCategories" to adRequestInfo.itemCategories,
    "stPlacement" to adRequestInfo.placement,
    "stCurrentCategory" to adRequestInfo.categories.joinToString(separator = ","),
    "stApiKey" to Storyteller.currentApiKey
  )
}
