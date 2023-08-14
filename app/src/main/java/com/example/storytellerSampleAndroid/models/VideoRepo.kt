package com.example.storytellerSampleAndroid.models

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class VideoRepo {

  private val client = HttpClient(CIO){
    install(ContentNegotiation){
      json(json = Json { ignoreUnknownKeys = true } )
    }
  }

  suspend fun getVerticalVideosList() : VerticalVideoListDto
    = client.get("https://sampleappcontent.usestoryteller.com/api/entries").body()
}
