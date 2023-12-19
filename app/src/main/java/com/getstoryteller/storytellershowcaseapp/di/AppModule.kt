package com.getstoryteller.storytellershowcaseapp.di

import android.content.Context
import com.getstoryteller.storytellershowcaseapp.api.ApiService
import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository
import com.getstoryteller.storytellershowcaseapp.data.repo.AuthRepositoryImpl
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.data.repo.TenantRepositoryImpl
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
import com.getstoryteller.storytellershowcaseapp.services.SessionService
import com.getstoryteller.storytellershowcaseapp.services.StorytellerService
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
    fun provideAuthRepository(apiService: ApiService): AuthRepository =
        AuthRepositoryImpl(apiService)

    @Provides
    fun provideTenantRepository(apiService: ApiService): TenantRepository =
        TenantRepositoryImpl(apiService)

    @Provides
    fun provideVerifyCodeUseCase(
      authRepository: AuthRepository,
      sessionService: SessionService,
      storytellerService: StorytellerService
    ): VerifyCodeUseCase =
        VerifyCodeUseCaseImpl(authRepository, sessionService, storytellerService)

    @Provides
    fun provideGetTenantSettings(
        tenantRepository: TenantRepository,
        storytellerService: StorytellerService
    ): GetTenantSettingsUseCase =
        GetTenantSettingsUseCaseImpl(tenantRepository, storytellerService)

    @Provides
    fun provideGetHomeScreenUseCase(
        tenantRepository: TenantRepository
    ): GetHomeScreenUseCase =
        GetHomeScreenUseCaseImpl(tenantRepository)

    @Provides
    fun provideLogoutUseCase(
        sessionService: SessionService
    ): LogoutUseCase = LogoutUseCaseImpl(sessionService)

    @Provides
    fun provideGetConfigUseCase(
        tenantRepository: TenantRepository,
        @ApplicationContext context: Context
    ): GetConfigurationUseCase = GetConfigurationUseCaseImpl(tenantRepository, context)

    @Provides
    fun provideGetTabContentUseCase(
        tenantRepository: TenantRepository
    ): GetTabContentUseCase = GetTabContentUseCaseImpl(tenantRepository)
}
