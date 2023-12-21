package com.getstoryteller.storytellershowcaseapp.di

import android.content.Context
import com.getstoryteller.storytellershowcaseapp.ShowcaseApp
import com.getstoryteller.storytellershowcaseapp.ads.managers.provider.google.GoogleNativeAdsManager
import com.getstoryteller.storytellershowcaseapp.services.ShowcaseStorytellerDelegate
import com.getstoryteller.storytellershowcaseapp.services.SessionService
import com.getstoryteller.storytellershowcaseapp.services.SessionServiceImpl
import com.getstoryteller.storytellershowcaseapp.services.StorytellerService
import com.getstoryteller.storytellershowcaseapp.services.StorytellerServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// This is a standard Hilt Module that configures the service layer

object ServicesModule {

  @Module
  @InstallIn(SingletonComponent::class)
  object ServicesProviderModule {
    @Singleton
    @Provides
    fun provideSessionService(@ApplicationContext context: Context): SessionService =
      SessionServiceImpl(
        context.getSharedPreferences(ShowcaseApp.PREFS_NAME, Context.MODE_PRIVATE)
      )

    @Provides
    @Singleton
    fun provideNativeAdsManager(
      @ApplicationContext context: Context
    ): GoogleNativeAdsManager = GoogleNativeAdsManager(context)

    @Singleton
    @Provides
    fun provideStorytellerService(
      sessionService: SessionService,
      showcaseStorytellerDelegate: ShowcaseStorytellerDelegate
    ): StorytellerService =
      StorytellerServiceImpl(
        sessionService,
        showcaseStorytellerDelegate
      )

  }
}
