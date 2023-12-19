package com.getstoryteller.storytellershowcaseapp.api

import com.getstoryteller.storytellershowcaseapp.data.entities.KeyValueDto
import com.getstoryteller.storytellershowcaseapp.data.entities.ResponseApiDto
import com.getstoryteller.storytellershowcaseapp.data.entities.ResponseApiListDto
import com.getstoryteller.storytellershowcaseapp.data.entities.StorytellerItemApiDto
import com.getstoryteller.storytellershowcaseapp.data.entities.TabDto
import com.getstoryteller.storytellershowcaseapp.data.entities.TenantSettingsApiDto
import com.getstoryteller.storytellershowcaseapp.services.SessionService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

// This class communicates with a separate API to fetch information about which Stories and
// Clips content to display.
// None of the code here is required to use the Storyteller SDK - rather it illustrates
// how to communicate with an external API.

@Singleton
class ApiService @Inject constructor(
  private val client: HttpClient,
  private val sessionService: SessionService
) {

  suspend fun verifyCode(code: String): ResponseApiDto<TenantSettingsApiDto> =
    client.post("validateCode") {
      setBody(mapOf("code" to code))
      contentType(ContentType.Application.Json)
    }.body()

  suspend fun getTenantSettings(): ResponseApiDto<TenantSettingsApiDto> =
    client.get("settings?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getLanguages(): ResponseApiListDto<KeyValueDto> =
    client.get("languages?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getTeams(): ResponseApiListDto<KeyValueDto> =
    client.get("teams?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getTabs(): ResponseApiListDto<TabDto> =
    client.get("tabs?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getTabById(tabId: String): ResponseApiListDto<StorytellerItemApiDto> =
    client.get("tabs/${tabId}?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getHomeItems(): ResponseApiListDto<StorytellerItemApiDto> =
    client.get("home?apiKey=${sessionService.apiKey}")
      .body()
}
