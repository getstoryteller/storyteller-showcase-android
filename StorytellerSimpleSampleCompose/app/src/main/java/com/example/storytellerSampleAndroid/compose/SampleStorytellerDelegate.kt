package com.example.storytellerSampleAndroid.compose

import com.storyteller.ui.list.StorytellerDelegate
import kotlinx.coroutines.flow.Flow

interface SampleStorytellerDelegate : StorytellerDelegate {
  val userNavigatedToApp: Flow<String>
}