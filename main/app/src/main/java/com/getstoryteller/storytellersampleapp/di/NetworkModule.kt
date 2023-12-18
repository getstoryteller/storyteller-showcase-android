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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

// This is a standard Hilt Module that configures the API layer

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @OptIn(ExperimentalSerializationApi::class)
  @Singleton
  @Provides
  fun provideHttpClient(): HttpClient = HttpClient(CIO) {
    engine {
      requestTimeout = 0
    }
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
