package com.getstoryteller.storytellersampleapp.api

import com.getstoryteller.storytellersampleapp.data.KeyValueDto
import com.getstoryteller.storytellersampleapp.data.ResponseApiDto
import com.getstoryteller.storytellersampleapp.data.ResponseApiListDto
import com.getstoryteller.storytellersampleapp.data.StorytellerItemApiDto
import com.getstoryteller.storytellersampleapp.data.TabDto
import com.getstoryteller.storytellersampleapp.data.TenantSettingsApiDto
import com.getstoryteller.storytellersampleapp.services.SessionService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(
    private val client: HttpClient,
    private val sessionService: SessionService
) {
    suspend fun verifyCode(code: String): ResponseApiDto<TenantSettingsApiDto> =
        client.post("https://sampleappcontent.usestoryteller.com/api/validateCode") {
            body = mapOf("code" to code)
            contentType(ContentType.Application.Json)
        }

    suspend fun getTenantSettings(): ResponseApiDto<TenantSettingsApiDto> =
        client.get("https://sampleappcontent.usestoryteller.com/api/settings?apiKey=${sessionService.apiKey}")

    suspend fun getLanguages(): ResponseApiListDto<KeyValueDto> =
        client.get("https://sampleappcontent.usestoryteller.com/api/languages?apiKey=${sessionService.apiKey}")

    suspend fun getTeams(): ResponseApiListDto<KeyValueDto> =
        client.get("https://sampleappcontent.usestoryteller.com/api/teams?apiKey=${sessionService.apiKey}")

    suspend fun getTabs(): ResponseApiListDto<TabDto> =
        client.get("https://sampleappcontent.usestoryteller.com/api/tabs?apiKey=${sessionService.apiKey}")

    suspend fun getTabById(tabId: String): ResponseApiListDto<StorytellerItemApiDto> =
        client.get("https://sampleappcontent.usestoryteller.com/api/tabs/${tabId}?apiKey=${sessionService.apiKey}")

    suspend fun getHomeItems(): ResponseApiListDto<StorytellerItemApiDto> =
        client.get("https://sampleappcontent.usestoryteller.com/api/home?apiKey=${sessionService.apiKey}")
}