package com.getstoryteller.storytellershowcaseapp.ui.di

import android.content.Context
import com.getstoryteller.storytellershowcaseapp.data.AuthRepositoryImpl
import com.getstoryteller.storytellershowcaseapp.data.TenantRepositoryImpl
import com.getstoryteller.storytellershowcaseapp.domain.GetConfigurationUseCase
import com.getstoryteller.storytellershowcaseapp.domain.GetConfigurationUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.GetHomeScreenUseCase
import com.getstoryteller.storytellershowcaseapp.domain.GetHomeScreenUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.GetTabContentUseCase
import com.getstoryteller.storytellershowcaseapp.domain.GetTabContentUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.GetTenantSettingsUseCase
import com.getstoryteller.storytellershowcaseapp.domain.GetTenantSettingsUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.LogoutUseCase
import com.getstoryteller.storytellershowcaseapp.domain.LogoutUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellershowcaseapp.domain.VerifyCodeUseCaseImpl
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

// This is a standard Hilt Module that configures the application layer

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
  @Provides
  fun provideAuthRepository(
    apiService: ApiService,
  ): AuthRepository = AuthRepositoryImpl(apiService)

  @Provides
  fun provideTenantRepository(
    apiService: ApiService,
  ): TenantRepository = TenantRepositoryImpl(apiService)

  @Provides
  fun provideVerifyCodeUseCase(
    authRepository: AuthRepository,
    sessionRepository: SessionRepository,
    storytellerService: StorytellerService,
    amplitudeService: AmplitudeService,
  ): VerifyCodeUseCase = VerifyCodeUseCaseImpl(authRepository, sessionRepository, storytellerService, amplitudeService)

  @Provides
  fun provideGetTenantSettings(
    tenantRepository: TenantRepository,
    storytellerService: StorytellerService,
  ): GetTenantSettingsUseCase = GetTenantSettingsUseCaseImpl(tenantRepository, storytellerService)

  @Provides
  fun provideGetHomeScreenUseCase(
    tenantRepository: TenantRepository,
  ): GetHomeScreenUseCase = GetHomeScreenUseCaseImpl(tenantRepository)

  @Provides
  fun provideLogoutUseCase(
    sessionRepository: SessionRepository,
    amplitudeService: AmplitudeService,
  ): LogoutUseCase = LogoutUseCaseImpl(sessionRepository, amplitudeService)

  @Provides
  fun provideGetConfigUseCase(
    tenantRepository: TenantRepository,
    @ApplicationContext context: Context,
  ): GetConfigurationUseCase = GetConfigurationUseCaseImpl(tenantRepository, context)

  @Provides
  fun provideGetTabContentUseCase(
    tenantRepository: TenantRepository,
  ): GetTabContentUseCase = GetTabContentUseCaseImpl(tenantRepository)
}
