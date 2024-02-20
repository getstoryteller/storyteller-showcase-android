package com.getstoryteller.storytellershowcaseapp.domain.ports

import com.getstoryteller.storytellershowcaseapp.domain.model.CategoriesModel
import com.getstoryteller.storytellershowcaseapp.domain.model.CollectionModel

interface SessionRepository {
  var apiKey: String?
  var userId: String?
  var language: String?
  var team: String?
  var hasAccount: Boolean
  var trackEvents: Boolean
  var categories: CategoriesModel
  var collection: CollectionModel

  fun reset()
}
