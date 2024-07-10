package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel

@Composable
fun OptionSelectScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  viewModel: OptionSelectViewModel,
  sharedViewModel: MainViewModel,
  optionUrlName: String,
  config: Config,
) {
  LaunchedEffect(
    key1 = optionUrlName,
    block = {
      viewModel.setupOptionType(config, optionUrlName)
    },
  )

  val reload by viewModel.reloadMainScreen.observeAsState()

  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(reload) {
    reload?.let {
      if (!uiState.allowMultiple) {
        navController.navigateUp()
      }
      sharedViewModel.refreshMainPage()
      viewModel.onReloadingDone()
    }
  }

  LazyColumn(
    modifier =
    modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colorScheme.surface)
      .padding(top = 20.dp)
      .navigationBarsPadding(),
  ) {
    items(uiState.options, key = { it.key ?: uiState.options.indexOf(it) }) { model ->
      Row(
        modifier =
        Modifier
          .fillMaxWidth()
          .height(56.dp)
          .background(color = MaterialTheme.colorScheme.tertiaryContainer)
          .clickable {
            viewModel.selectOption(model.value)
          },
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (!uiState.allowMultiple) {
          RadioButton(
            selected = uiState.selectedOption == model.value,
            onClick = {
              viewModel.selectOption(model.value)
            },
          )
        } else {
          val optionChecked = uiState.selectedOptions.contains(model.value)
          Checkbox(
            checked = optionChecked,
            onCheckedChange = { checked ->
              viewModel.selectOptionMultiple(model.value, checked)
            },
          )
        }
        Text(
          color = MaterialTheme.colorScheme.onBackground,
          text = model.key ?: "Not set",
        )
      }
    }
  }
}

const val EVENT_TRACKING = "event_tracking"
