package com.getstoryteller.storytellersampleapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
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
            .padding(top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Start,
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
                text = moreButtonTitle
            )
            Icon(
                Icons.Filled.ArrowForward,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(12.dp),
                contentDescription = null,
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
