package com.getstoryteller.storytellersampleapp.api

import com.getstoryteller.storytellersampleapp.data.entities.KeyValueDto
import com.getstoryteller.storytellersampleapp.data.entities.ResponseApiDto
import com.getstoryteller.storytellersampleapp.data.entities.ResponseApiListDto
import com.getstoryteller.storytellersampleapp.data.entities.StorytellerItemApiDto
import com.getstoryteller.storytellersampleapp.data.entities.TabDto
import com.getstoryteller.storytellersampleapp.data.entities.TenantSettingsApiDto
import com.getstoryteller.storytellersampleapp.services.SessionService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

// This class communicates with a separate API to fetch information about which Stories and
// Clips content to display.
// None of the code here is required to use the Storyteller SDK - rather it illustrates
// how to communicate with an external API.

class ApiService(
  private val client: HttpClient,
  private val sessionService: SessionService
) {

  suspend fun verifyCode(code: String): ResponseApiDto<TenantSettingsApiDto> =
    client.post("https://sampleappcontent.usestoryteller.com/api/validateCode") {
      setBody(mapOf("code" to code))
      contentType(ContentType.Application.Json)
    }.body()

  suspend fun getTenantSettings(): ResponseApiDto<TenantSettingsApiDto> =
    client.get("https://sampleappcontent.usestoryteller.com/api/settings?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getLanguages(): ResponseApiListDto<KeyValueDto> =
    client.get("https://sampleappcontent.usestoryteller.com/api/languages?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getTeams(): ResponseApiListDto<KeyValueDto> =
    client.get("https://sampleappcontent.usestoryteller.com/api/teams?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getTabs(): ResponseApiListDto<TabDto> =
    client.get("https://sampleappcontent.usestoryteller.com/api/tabs?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getTabById(tabId: String): ResponseApiListDto<StorytellerItemApiDto> =
    client.get("https://sampleappcontent.usestoryteller.com/api/tabs/${tabId}?apiKey=${sessionService.apiKey}")
      .body()

  suspend fun getHomeItems(): ResponseApiListDto<StorytellerItemApiDto> =
    client.get("https://sampleappcontent.usestoryteller.com/api/home?apiKey=${sessionService.apiKey}")
      .body()
}
