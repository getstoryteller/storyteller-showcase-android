package com.getstoryteller.storytellersampleapp.features.account

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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
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
import com.getstoryteller.storytellersampleapp.R
import com.getstoryteller.storytellersampleapp.domain.Config
import com.storyteller.ui.pager.StorytellerClipsFragment


@Composable
fun AccountScreen(
  navController: NavController,
  viewModel: AccountViewModel,
  config: Config?,
  onLogout: () -> Unit,
  onRefresh: () -> Unit
) {
  val localContext = LocalContext.current
  val isLoggedOut by viewModel.isLoggedOut.collectAsState()
  val refreshMainPage by viewModel.refreshMainPage.collectAsState()
  if (isLoggedOut) {
    navController.navigateUp()
    onLogout()
  }
  if (refreshMainPage) {
    navController.navigateUp()
    onRefresh()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        color = MaterialTheme.colors.surface
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
          color = MaterialTheme.colors.onSurface
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
        color = MaterialTheme.colors.onSurface
      )
      SettingsRow(
        text = "Allow Event Tracking",
        arrowVisible = true,
        onClick = {
          navController.navigate("account/${OptionSelectType.EVENT_TRACKING.name}")
        }
      )
      SettingsRow(
        text = "Reset",
        onClick = {
          viewModel.reset()
          navController.navigateUp()
        }
      )
      SettingsRow(
        text = "Log Out",
        color = colorResource(id = R.color.error),
        onClick = {
          // remove the moments fragment to avoid glitches when logging out and in with a new code
          val fmgr = (localContext as? FragmentActivity)?.supportFragmentManager
          val existingFragment = fmgr?.findFragmentByTag("watch") as? StorytellerClipsFragment
          if (existingFragment != null) {
            fmgr.beginTransaction().remove(existingFragment).commit()
          }

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
  val isDarkTheme = isSystemInDarkTheme()
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(56.dp)
      .background(if (isDarkTheme) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.background)
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
      color = MaterialTheme.colors.onBackground
    )
    if (arrowVisible) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "",
        modifier = Modifier.padding(end = 16.dp),
        tint = MaterialTheme.colors.onBackground
      )
    }
  }
}
