package com.example.storytellerSampleAndroid.compose.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.storytellerSampleAndroid.R
import com.storyteller.Storyteller

@Composable
fun TopBar() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .background(MaterialTheme.colorScheme.primaryContainer)
      .padding(8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    val context = LocalContext.current

    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
      Text(text = "Storyteller Compose")
      Image(
        modifier = Modifier
          .size(16.dp)
          .align(Alignment.Bottom),
        painter = painterResource(id = R.drawable.ic_storyteller),
        contentDescription = "Storyteller Icon"
      )
      Text(
        modifier = Modifier.align(Alignment.Bottom),
        text = "v${Storyteller.version.calculateVersion()}",
        style = MaterialTheme.typography.bodySmall
      )
    }
    IconButton(onClick = {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("https://getstoryteller.com")
      context.startActivity(intent)
    }) {
      Image(
        painter = painterResource(id = R.drawable.ic_storyteller),
        contentDescription = "Storyteller Logo"
      )
    }
  }
}

private fun String.calculateVersion(): String {
  val substringBefore = this.substringBefore("-")
  val localPart  = if (this.contains("local", true)) "-local" else ""
  return substringBefore + localPart
}