package com.getstoryteller.storytellershowcaseapp.ads.kvps

import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo

interface StorytellerKVPProvider<T : StorytellerAdRequestInfo> {
  fun getKVPs(adRequestInfo: T): Map<String, String>

  val StorytellerAdRequestInfo.itemCategories
    get() = itemInfo.categories.mapNotNull { it.externalId }.filter { it.isNotEmpty() }
      .joinToString(",")
}
