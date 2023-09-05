package com.example.storytellerSampleAndroid.models

import com.example.storytellerSampleAndroid.preferences.SharedPreferencesManager


class GetVideoListUseCase(private val videoRepo: VideoRepo = VideoRepo(),
                          private val sharedPreferencesManager: SharedPreferencesManager) {

  suspend operator fun invoke(): List<Item> {
    val items = videoRepo.getVerticalVideosList()
      .mapNotNull {
        val videoListItem = it.copy(
          collection = it.collection?.replace("[LANGUAGE]", sharedPreferencesManager.language),
          categories = it.categories?.map { category -> category.replace("[LANGUAGE]", sharedPreferencesManager.language) }
        )
        videoListItem.toEntity
      }
    return items
  }
}