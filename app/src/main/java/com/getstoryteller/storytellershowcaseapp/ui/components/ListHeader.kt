package com.getstoryteller.storytellershowcaseapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getstoryteller.storytellershowcaseapp.ui.LocalStorytellerColorsPalette

@Composable
fun ListHeader(
  text: String,
  moreButtonTitle: String,
  collectionId: String? = null,
  categories: List<String> = emptyList(),
  onMoreClicked: (String?, List<String>) -> Unit = { _, _ -> }
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      modifier = Modifier
        .fillMaxHeight()
        .weight(1F),
      text = text,
      textAlign = TextAlign.Start,
      fontSize = 20.sp,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      fontWeight = FontWeight.W700,
      color = LocalStorytellerColorsPalette.current.header
    )
    Text(
      modifier = Modifier
        .fillMaxHeight()
        .wrapContentWidth()
        .clickable {
          onMoreClicked(collectionId, categories)
        }
        .padding(start = 12.dp),
      text = moreButtonTitle,
      fontWeight = FontWeight.W400,
      fontSize = 16.sp,
      color = MaterialTheme.colors.secondaryVariant
    )
  }
}
