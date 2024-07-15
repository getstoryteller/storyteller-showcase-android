package com.getstoryteller.storytellershowcaseapp.ui.features.home

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import coil.transform.RoundedCornersTransformation
import com.getstoryteller.storytellershowcaseapp.ui.features.main.findActivity
import com.storyteller.Storyteller

@Composable
fun ImageActionItem(
  uiModel: ImageItemUiModel,
) {
  val isDarkTheme = isSystemInDarkTheme()
  val url = if (isDarkTheme) uiModel.darkModeUrl else uiModel.url
  val activity = LocalContext.current.findActivity()
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 12.dp, end = 12.dp),
  ) {
    AsyncImage(
      modifier = Modifier.fillMaxWidth()
        .clickable {
          if (uiModel.action != null && uiModel.action.type == "inApp") {
            val uri = Uri.parse(uiModel.action.url)
            val category = uri?.getQueryParameter("categoryId")
            if (category != null) {
              activity?.let {
                Storyteller.openCategory(it, category)
              }
            } else {
              return@clickable
            }
          }
        },
      model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .size(Size.ORIGINAL)
        .scale(Scale.FIT)
        .transformations(RoundedCornersTransformation(28f))
        .crossfade(true)
        .build(),
      contentScale = ContentScale.FillWidth,
      contentDescription = "",
    )
  }
}
