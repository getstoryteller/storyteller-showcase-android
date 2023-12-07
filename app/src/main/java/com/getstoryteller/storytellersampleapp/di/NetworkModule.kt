package com.getstoryteller.storytellersampleapp.di

import com.getstoryteller.storytellersampleapp.api.ApiService
import com.getstoryteller.storytellersampleapp.services.SessionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Singleton
  @Provides
  fun provideHttpClient() = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
      })
    }
  }

  @Provides
  fun provideApiService(
    client: HttpClient,
    sessionService: SessionService
  ): ApiService = ApiService(client, sessionService)
}
