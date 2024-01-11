package com.getstoryteller.storytellershowcaseapp.ui.features.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel

@Composable
fun LoginScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier,
  onLoggedIn: () -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    LoginDialog(viewModel, onLoggedIn)
  }
}
