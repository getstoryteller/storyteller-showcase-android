package com.getstoryteller.storytellershowcaseapp.ui.features.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel, onLoggedIn: () -> Unit) {
  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
    LoginDialog(viewModel, onLoggedIn)
  }
}
