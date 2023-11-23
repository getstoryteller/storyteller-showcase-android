package com.getstoryteller.storytellersampleapp.di

import android.content.Context
import com.getstoryteller.storytellersampleapp.SampleApp
import com.getstoryteller.storytellersampleapp.services.SessionService
import com.getstoryteller.storytellersampleapp.services.SessionServiceImpl
import com.getstoryteller.storytellersampleapp.services.StorytellerService
import com.getstoryteller.storytellersampleapp.services.StorytellerServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Singleton
    @Provides
    fun provideSessionService(@ApplicationContext context: Context): SessionService =
        SessionServiceImpl(
            context.getSharedPreferences(SampleApp.PREFS_NAME, Context.MODE_PRIVATE)
        )

    @Singleton
    @Provides
    fun provideStorytellerService(sessionService: SessionService): StorytellerService =
        StorytellerServiceImpl(
            sessionService
        )

}