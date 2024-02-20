package com.getstoryteller.storytellershowcaseapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionModel(
  val title: String,
  val collection: String,
)
