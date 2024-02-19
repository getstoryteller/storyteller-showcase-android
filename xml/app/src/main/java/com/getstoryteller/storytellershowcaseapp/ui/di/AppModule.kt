package com.getstoryteller.storytellershowcaseapp.ui.di

import com.getstoryteller.storytellershowcaseapp.data.AuthRepositoryImpl
import com.getstoryteller.storytellershowcaseapp.domain.GetDemoDataUseCase
import com.getstoryteller.storytellershowcaseapp.domain.GetDemoDataUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellershowcaseapp.domain.VerifyCodeUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.getstoryteller.storytellershowcaseapp.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

// This is a standard Hilt Module that configures the application layer
@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
  @Provides
  fun provideAuthRepository(
    apiService: ApiService,
  ): AuthRepository = AuthRepositoryImpl(apiService)

  @Provides
  fun provideVerifyCodeUseCase(
    authRepository: AuthRepository,
    sessionRepository: SessionRepository,
    storytellerService: StorytellerService,
    amplitudeService: AmplitudeService,
  ): VerifyCodeUseCase = VerifyCodeUseCaseImpl(authRepository, sessionRepository, storytellerService, amplitudeService)

  @Provides
  fun provideGetDemoDataUseCase(): GetDemoDataUseCase = GetDemoDataUseCaseImpl()
}
