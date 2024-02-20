package com.getstoryteller.storytellershowcaseapp.ui.di

import com.getstoryteller.storytellershowcaseapp.BuildConfig
import com.storyteller.domain.entities.Error
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

// This is a standard Hilt Module that configures the API (remote) layer

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @InternalAPI
  @OptIn(ExperimentalSerializationApi::class)
  @Singleton
  @Provides
  fun provideHttpClient(
    buildConfigSpecificSetup: BuildConfigSpecificSetup,
  ): HttpClient =
    HttpClient(OkHttp) {
      install(DefaultRequest) {
        url(BuildConfig.API_BASE_URL)
      }
      install(HttpRequestRetry) {
        maxRetries = 5
        retryIf { request, response ->
          !response.status.isSuccess()
        }
        retryOnExceptionIf { request, cause ->
          cause is Error.NetworkError
        }
        delayMillis { retry ->
          retry * 3000L
        } // retries in 3, 6, 9, etc. seconds
      }
      install(ContentNegotiation) {
        json(
          json = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
          },
        )
      }
      buildConfigSpecificSetup.networkModule(this)
      Logging {
        level = LogLevel.ALL
      }
    }

  @Provides
  @Singleton
  fun provideBuildConfigSpecificSetup(): BuildConfigSpecificSetup = BuildConfigSpecificSetupImpl()
}
