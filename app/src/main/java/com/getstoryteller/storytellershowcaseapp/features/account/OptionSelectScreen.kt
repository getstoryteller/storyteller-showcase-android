package com.getstoryteller.storytellershowcaseapp.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.features.main.MainViewModel

@Composable
fun OptionSelectScreen(
  navController: NavController,
  viewModel: OptionSelectViewModel,
  sharedViewModel: MainViewModel,
  optionSelectType: OptionSelectType,
  config: Config
) {
  val isDarkTheme = isSystemInDarkTheme()
  LaunchedEffect(key1 = optionSelectType.name, block = {
    viewModel.setupOptionType(config, optionSelectType)
  })

  val reload by viewModel.reloadMainScreen.observeAsState()

  LaunchedEffect(reload) {
    reload?.let {
      navController.navigateUp()
      sharedViewModel.refreshMainPage()
      viewModel.onReloadingDone()
    }
  }

  val uiState by viewModel.uiState.collectAsState()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(top = 20.dp)
      .background(color = MaterialTheme.colors.surface)
      .navigationBarsPadding()
  ) {
    items(uiState.options, key = { it.key ?: uiState.options.indexOf(it) }) { model ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(40.dp)
          .background(if (isDarkTheme) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.background)
          .clickable {
            viewModel.selectOption(model.key)
          },
        verticalAlignment = Alignment.CenterVertically
      ) {
        RadioButton(selected = uiState.selectedOption == model.key, onClick = {
          viewModel.selectOption(model.key)
        })
        Text(
          color = MaterialTheme.colors.onBackground,
          text = model.value
        )
      }
    }
  }
}

enum class OptionSelectType(val title: String) {
  HAS_ACCOUNT("Has Account"),
  LANGUAGE("Language"),
  TEAM("Favorite Team"),
  EVENT_TRACKING("Allow Event Tracking"),
}
