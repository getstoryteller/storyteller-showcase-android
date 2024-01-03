package com.getstoryteller.storytellershowcaseapp.ui.di

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import timber.log.Timber
import javax.inject.Inject

class BuildConfigSpecificSetupImpl @Inject constructor() : BuildConfigSpecificSetup {
  override fun networkModule(
    httpClientConfig: HttpClientConfig<OkHttpConfig>,
  ) {
    httpClientConfig.install(Logging) {
      logger = object : Logger by Logger.DEFAULT {
        override fun log(
          message: String,
        ) {
          Timber.tag("ST-LOG").d(message)
        }
      }
      level = LogLevel.INFO
    }
  }
}
