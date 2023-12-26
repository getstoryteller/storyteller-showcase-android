package com.getstoryteller.storytellershowcaseapp.ui.di

import android.content.Context
import android.content.SharedPreferences
import com.getstoryteller.storytellershowcaseapp.ShowcaseApp
import com.getstoryteller.storytellershowcaseapp.data.AmplitudeServiceImpl
import com.getstoryteller.storytellershowcaseapp.data.SessionRepositoryImpl
import com.getstoryteller.storytellershowcaseapp.data.StorytellerServiceImpl
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import dagger.Binds
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
  object ServicesProvidingModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(
      @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(ShowcaseApp.PREFS_NAME, Context.MODE_PRIVATE)
  }

  @Module
  @InstallIn(SingletonComponent::class)
  abstract class ServicesBindingModule {
    @Singleton
    @Binds
    abstract fun bindSessionService(impl: SessionRepositoryImpl): SessionRepository

    @Singleton
    @Binds
    abstract fun bindStorytellerService(impl: StorytellerServiceImpl): StorytellerService

    @Singleton
    @Binds
    abstract fun bindAmplitudeService(impl: AmplitudeServiceImpl): AmplitudeService
  }
}
