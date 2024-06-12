package com.getstoryteller.storytellershowcaseapp.ui.features

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.getstoryteller.storytellershowcaseapp.data.ShowcaseStorytellerDelegate
import com.getstoryteller.storytellershowcaseapp.ui.ShowcaseAppTheme
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var storytellerDelegate: ShowcaseStorytellerDelegate

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(
    savedInstanceState: Bundle?,
  ) {
    super.onCreate(savedInstanceState)
    viewModel.setup()
    val deepLinkData = intent?.data?.toString()
    viewModel.handleDeeplink(null)
    enableEdgeToEdge()
    setContent {
      val navController = rememberNavController()
      LaunchedEffect(Unit) {
        storytellerDelegate.onInterceptNavigation {
          val uriToPass = Uri.encode(it)
          navController.navigate("home/link/$uriToPass")
        }
      }

      ShowcaseAppTheme {
        MainScreen(
          activity = this,
          navController = navController,
          viewModel = viewModel,
          deepLinkData = deepLinkData,
          onLocationChanged = { storytellerDelegate.onLocationChanged { it } },
        )
      }
    }
  }
}
