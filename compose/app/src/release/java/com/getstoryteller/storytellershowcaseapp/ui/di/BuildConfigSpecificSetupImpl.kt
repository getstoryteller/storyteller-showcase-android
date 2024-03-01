package com.getstoryteller.storytellershowcaseapp.ui.di

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import javax.inject.Inject

class BuildConfigSpecificSetupImpl @Inject constructor() : BuildConfigSpecificSetup {
  override fun networkModule(
    httpClientConfig: HttpClientConfig<OkHttpConfig>,
  ) = Unit
}
