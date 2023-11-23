package com.getstoryteller.storytellersampleapp.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.R
import com.getstoryteller.storytellersampleapp.domain.Config

@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel,
    config: Config?,
    onLogout: () -> Unit
) {

    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    if (isLoggedOut) {
        navController.navigateUp()
        onLogout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.background_settings)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            config?.let {
                Text(
                    text = "PERSONALISATION",
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                    color = colorResource(id = R.color.text_label)
                )
                if (it.teams.isNotEmpty()) {
                    SettingsRow(
                        text = "Favorite Teams",
                        arrowVisible = true,
                        onClick = {
                            navController.navigate("account/${OptionSelectType.TEAM.name}")
                        }
                    )
                }
                if (it.languages.isNotEmpty()) {
                    SettingsRow(
                        text = "Language",
                        arrowVisible = true,
                        onClick = {
                            navController.navigate("account/${OptionSelectType.LANGUAGE.name}")
                        })
                }
                SettingsRow(
                    text = "Has Account",
                    arrowVisible = true,
                    onClick = {
                        navController.navigate("account/${OptionSelectType.HAS_ACCOUNT.name}")
                    })
            }
            Text(
                text = "SETTINGS",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                color = colorResource(id = R.color.text_label)
            )
            SettingsRow(text = "Reset")
            SettingsRow(
                text = "Log Out",
                color = colorResource(id = R.color.error),
                onClick = {
                    viewModel.logout()
                }
            )
        }
    }
}

@Composable
fun SettingsRow(
    text: String,
    arrowVisible: Boolean = false,
    color: Color = colorResource(id = R.color.on_light_color_active),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            color = color
        )
        if (arrowVisible) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.padding(end = 16.dp),
                tint = color
            )
        }
    }
}