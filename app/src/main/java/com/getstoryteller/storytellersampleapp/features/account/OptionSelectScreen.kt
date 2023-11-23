package com.getstoryteller.storytellersampleapp.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun OptionSelectScreen(
    navController: NavController,
    viewModel: OptionSelectViewModel,
    optionSelectType: OptionSelectType,
    config: Config
) {
    LaunchedEffect(key1 = optionSelectType.name, block = {
        viewModel.setupOptionType(config, optionSelectType)
    })

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .background(color = colorResource(id = R.color.background_settings))
    ) {
        uiState.options.forEach { model ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = Color.White)
                    .clickable {
                        viewModel.selectOption(model.key)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = uiState.selectedOption == model.key, onClick = {
                    viewModel.selectOption(model.key)
                })
                Text(model.value)
            }
        }
    }
}

enum class OptionSelectType(val title: String) {
    HAS_ACCOUNT("Has Account"),
    LANGUAGE("Language"),
    TEAM("Favorite Team")
}