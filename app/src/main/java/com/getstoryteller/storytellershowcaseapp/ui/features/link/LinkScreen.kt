package com.getstoryteller.storytellershowcaseapp.ui.features.link

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.ui.LocalStorytellerColorsPalette

@Composable
fun LinkScreen(
  modifier: Modifier = Modifier,
  link: String? = null,
) {
  Surface(modifier = modifier.fillMaxSize()) {
    Box {
      Column(
        modifier = modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_link),
          contentDescription = "Link icon",
          tint = LocalStorytellerColorsPalette.current.linkIconColor,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "This Link Would Navigate User To:",
          style = MaterialTheme.typography.bodyMedium,
          fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        )
        Text(
          text = link ?: "link not set",
          style = MaterialTheme.typography.bodyMedium,
          fontSize = MaterialTheme.typography.bodyLarge.fontSize,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}

@Composable
@Preview
fun LinkScreenPreview() {
  LinkScreen(link = "https://www.google.com")
}
