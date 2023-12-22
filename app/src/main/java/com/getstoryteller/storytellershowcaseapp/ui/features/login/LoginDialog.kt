package com.getstoryteller.storytellershowcaseapp.ui.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.ui.features.main.LoginState
import com.getstoryteller.storytellershowcaseapp.ui.features.main.LoginState.Error
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.getstoryteller.storytellershowcaseapp.ui.LocalStorytellerColorsPalette

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginDialog(
  viewModel: MainViewModel, onLoggedIn: () -> Unit
) {
  val loginUiState by viewModel.loginUiState.collectAsState()
  val loginState = loginUiState.loginState

  LaunchedEffect(loginUiState.isLoggedIn) {
    if (loginUiState.isLoggedIn) {
      onLoggedIn()
    }
  }

  Dialog(
    onDismissRequest = {}, properties = DialogProperties(
      dismissOnBackPress = false,
      dismissOnClickOutside = false,
      usePlatformDefaultWidth = false,
    )
  ) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 10.dp, end = 10.dp),
      shape = RoundedCornerShape(8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        var text by rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(loginUiState.loginState) {
          if (loginUiState.loginState is LoginState.Error) {
            focusRequester.requestFocus()
            keyboardController?.show()
          }
        }

        Image(
          modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
          painter = painterResource(id = if (isDarkTheme) R.drawable.ic_logo_dark else R.drawable.ic_logo),
          contentDescription = "Logo of Storyteller"
        )
        Text(
          modifier = Modifier
            .padding(top = 16.dp)
            .align(alignment = Alignment.CenterHorizontally),
          text = stringResource(id = R.string.label_login_description)
        )

        OutlinedTextField(modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
          .focusRequester(focusRequester),
          value = text,
          onValueChange = {
            text = it
            viewModel.clearErrorState()
          },
          keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              viewModel.verifyCode(text)
              keyboardController?.hide()
            }
          ),
          singleLine = true,
          isError = loginState is Error,
          placeholder = { Text(text = stringResource(id = R.string.label_login_enter_code)) },
          colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = LocalStorytellerColorsPalette.current.background),
          leadingIcon = {
            Image(
              painter = painterResource(id = R.drawable.ic_key), contentDescription = ""
            )
          })


        if (loginState is Error) {
          val annotatedText = buildAnnotatedString {
            appendInlineContent("leadingIcon", "[icon]")
            append(" ${loginState.message}")
          }

          val inlineContent = mapOf(
            Pair("leadingIcon", InlineTextContent(
              Placeholder(
                width = 12.sp, height = 12.sp, placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
              )
            ) {
              Icon(painterResource(id = R.drawable.ic_error), "", tint = colors.error)
            })
          )

          Text(
            text = annotatedText,
            color = colors.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp),
            inlineContent = inlineContent,
          )
        }

        Button(modifier = Modifier
          .height(48.dp)
          .padding(top = 8.dp)
          .fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = colors.primary,
            disabledContentColor = colors.onPrimary
          ),
          enabled = !loginUiState.isLoggedIn && loginState !is LoginState.Loading,
          onClick = {
            viewModel.verifyCode(text)
            keyboardController?.hide()
          }) {
          when (loginState) {
            LoginState.Loading, LoginState.Success -> {
              CircularProgressIndicator(
                modifier = Modifier.size(24.dp), color = colors.onPrimary
              )
            }
            else -> {
              Text(text = stringResource(id = R.string.action_login_verify))
            }
          }
        }
      }
    }
  }
}
