package com.getstoryteller.storytellershowcaseapp.ui.di

import com.getstoryteller.storytellershowcaseapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

// This is a standard Hilt Module that configures the API (remote) layer

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @OptIn(ExperimentalSerializationApi::class)
  @Singleton
  @Provides
  fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
    install(DefaultRequest) {
      url(BuildConfig.API_BASE_URL)
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
}
