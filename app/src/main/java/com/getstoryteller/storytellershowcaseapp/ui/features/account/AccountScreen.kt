package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.ui.LocalStorytellerColorsPalette
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.getstoryteller.storytellershowcaseapp.ui.utils.copyToClipboard
import com.getstoryteller.storytellershowcaseapp.ui.utils.formatterApplicationVersion
import com.getstoryteller.storytellershowcaseapp.ui.utils.toast
import com.storyteller.ui.pager.StorytellerClipsFragment

// This view demonstrates how to pass User Attributes to the Storyteller SDK
// for the purposes of personalization and targeting of stories.
// The corresponding code which interacts with the Storyteller SDK is
// visible in the StorytellerService class.
// There is more information available about this feature in our
// documentation here https://www.getstoryteller.com/documentation/android/custom-attributes

// The code here also shows to enable and disable event tracking for
// the Storyteller SDK. The corresponding code which interacts with the
// Storyteller SDK is visible in the StorytellerService class.

@Composable
fun AccountScreen(
  navController: NavController,
  viewModel: AccountViewModel,
  sharedViewModel: MainViewModel,
  config: Config?,
  onLogout: () -> Unit
) {
  val context = LocalContext.current
  val isLoggedOut by viewModel.isLoggedOut.collectAsState()
  LaunchedEffect(isLoggedOut) {
    if (isLoggedOut) {
      onLogout()
      navController.navigate("home")
    }
  }
  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(
            color = MaterialTheme.colorScheme.surface
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
          color = MaterialTheme.colorScheme.onSurface
        )
        if (it.teams.isNotEmpty()) {
          SettingsRow(text = "Favorite Team", arrowVisible = true, onClick = {
            navController.navigate("account/${OptionSelectType.TEAM.name}")
          })
        }
        if (it.languages.isNotEmpty()) {
          SettingsRow(text = "Language", arrowVisible = true, onClick = {
            navController.navigate("account/${OptionSelectType.LANGUAGE.name}")
          })
        }
        SettingsRow(text = "Has Account", arrowVisible = true, onClick = {
          navController.navigate("account/${OptionSelectType.HAS_ACCOUNT.name}")
        })
      }
      Text(
        text = "SETTINGS",
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.onSurface
      )
      SettingsRow(text = "Allow Event Tracking", arrowVisible = true, onClick = {
        navController.navigate("account/${OptionSelectType.EVENT_TRACKING.name}")
      })
      SettingsRow(text = "Reset", onClick = {
        viewModel.reset()
        sharedViewModel.refreshMainPage()
        navController.navigateUp()
        context.toast("Reset successful")
      })
      SettingsRow(text = "Log Out", color = colorResource(id = R.color.error), onClick = {
        // remove the moments fragment to avoid glitches when logging out and in with a new code
        val fmgr = (context as? FragmentActivity)?.supportFragmentManager
        val existingFragment = fmgr?.findFragmentByTag("moments") as? StorytellerClipsFragment
        if (existingFragment != null) {
          fmgr.beginTransaction().remove(existingFragment).commit()
        }
        navController.navigate("home")
        viewModel.logout()
      })
      Text(
        text = "APP INFO",
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.onSurface
      )
      SettingsRow(text = "Version", onClick = {
        context.copyToClipboard(context.formatterApplicationVersion)
        context.toast("App version copied to clipboard")
      }) {
        Text(
          text = context.formatterApplicationVersion,
          modifier = Modifier.padding(end = 16.dp),
          color = LocalStorytellerColorsPalette.current.subtitle
        )
      }
    }
  }
}


@Composable
fun SettingsRow(
  text: String,
  arrowVisible: Boolean = false,
  color: Color = MaterialTheme.colorScheme.onBackground,
  onClick: () -> Unit = {},
  content: @Composable () -> Unit = {}
) {
  val isDarkTheme = isSystemInDarkTheme()
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .background(if (isDarkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
        .clickable {
            onClick()
        }, verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = text,
      color = color,
      modifier = Modifier
          .padding(start = 16.dp)
          .weight(1f),
    )
    if (arrowVisible) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "",
        modifier = Modifier.padding(end = 16.dp),
        tint = MaterialTheme.colorScheme.onBackground
      )
    }
    content()
  }
}
