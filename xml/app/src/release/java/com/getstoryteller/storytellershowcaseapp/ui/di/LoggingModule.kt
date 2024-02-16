package com.getstoryteller.storytellershowcaseapp.ui.di

import com.getstoryteller.storytellershowcaseapp.analytics.CrashReportingTree
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

  @Provides
  @Singleton
  fun provideTimberTree(): Timber.Tree = CrashReportingTree()
}
