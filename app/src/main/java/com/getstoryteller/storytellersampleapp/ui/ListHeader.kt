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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.onBackground
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
                color = MaterialTheme.colors.onBackground
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
