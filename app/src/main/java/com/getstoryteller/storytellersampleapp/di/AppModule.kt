package com.getstoryteller.storytellersampleapp.di

import com.getstoryteller.storytellersampleapp.api.ApiService
import com.getstoryteller.storytellersampleapp.data.repo.AuthRepository
import com.getstoryteller.storytellersampleapp.data.repo.AuthRepositoryImpl
import com.getstoryteller.storytellersampleapp.data.repo.TenantRepository
import com.getstoryteller.storytellersampleapp.data.repo.TenantRepositoryImpl
import com.getstoryteller.storytellersampleapp.domain.GetConfigurationUseCase
import com.getstoryteller.storytellersampleapp.domain.GetConfigurationUseCaseImpl
import com.getstoryteller.storytellersampleapp.domain.GetHomeScreenUseCase
import com.getstoryteller.storytellersampleapp.domain.GetHomeScreenUseCaseImpl
import com.getstoryteller.storytellersampleapp.domain.GetTabContentUseCase
import com.getstoryteller.storytellersampleapp.domain.GetTabContentUseCaseImpl
import com.getstoryteller.storytellersampleapp.domain.GetTenantSettingsUseCase
import com.getstoryteller.storytellersampleapp.domain.GetTenantSettingsUseCaseImpl
import com.getstoryteller.storytellersampleapp.domain.LogoutUseCase
import com.getstoryteller.storytellersampleapp.domain.LogoutUseCaseImpl
import com.getstoryteller.storytellersampleapp.domain.VerifyCodeUseCase
import com.getstoryteller.storytellersampleapp.domain.VerifyCodeUseCaseImpl
import com.getstoryteller.storytellersampleapp.services.SessionService
import com.getstoryteller.storytellersampleapp.services.StorytellerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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
        tenantRepository: TenantRepository
    ): GetConfigurationUseCase = GetConfigurationUseCaseImpl(tenantRepository)

    @Provides
    fun provideGetTabContentUseCase(
        tenantRepository: TenantRepository
    ): GetTabContentUseCase = GetTabContentUseCaseImpl(tenantRepository)
}