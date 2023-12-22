package com.getstoryteller.storytellershowcaseapp.data.ads.kvps

import com.storyteller.Storyteller
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import javax.inject.Inject

class ClipsKVPsProvider @Inject constructor() :
  StorytellerKVPProvider<StorytellerAdRequestInfo.ClipsAdRequestInfo> {
  override fun getKVPs(adRequestInfo: StorytellerAdRequestInfo.ClipsAdRequestInfo) = mapOf(
    "stCollection" to adRequestInfo.collection,
    "stClipCategories" to adRequestInfo.itemCategories,
    "stClipId" to adRequestInfo.itemInfo.id,
    "stApiKey" to Storyteller.currentApiKey
  )
}
