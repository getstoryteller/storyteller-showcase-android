package com.getstoryteller.storytellersampleapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getstoryteller.storytellersampleapp.R

@Composable
fun ListHeader(
  text: String,
  moreButtonTitle: String = "More",
  collectionId: String? = null,
  categories: List<String> = emptyList(),
  onMoreClicked: (String?, List<String>) -> Unit = { _, _ -> }
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 12.dp, end = 0.dp, bottom = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = text,
      textAlign = TextAlign.Start,
      fontSize = 20.sp,
      fontWeight = FontWeight.W600,
      color = MaterialTheme.colors.secondaryVariant
    )
    Row(
      modifier = Modifier
        .fillMaxHeight()
        .clickable {
          onMoreClicked(collectionId, categories)
        },
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = moreButtonTitle,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondaryVariant
      )

      Icon(
        painterResource(id = R.drawable.baseline_chevron_right_24),
        contentDescription = null,
        tint = MaterialTheme.colors.secondaryVariant,
        modifier = Modifier
          .padding(start = 4.dp)
      )
    }
  }
}

private fun String.modify(moreTextCapitalized: Boolean): String {
  return if (moreTextCapitalized) {
    this.uppercase()
  } else {
    this
  }
}
