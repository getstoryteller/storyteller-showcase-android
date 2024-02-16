package com.getstoryteller.storytellershowcaseapp.ui.di

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig

interface BuildConfigSpecificSetup {
  fun networkModule(
    httpClientConfig: HttpClientConfig<OkHttpConfig>,
  )
}
