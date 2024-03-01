package com.getstoryteller.storytellershowcaseapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesModel(
  val title: String,
  val categories: List<String>,
)
