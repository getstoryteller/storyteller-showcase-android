package com.getstoryteller.storytellershowcaseapp.ui.features

import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.fragment.app.Fragment.SavedState
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.compose.rememberNavController
import com.getstoryteller.storytellershowcaseapp.data.ShowcaseStorytellerDelegate
import com.getstoryteller.storytellershowcaseapp.ui.ShowcaseAppTheme
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainScreen
import com.getstoryteller.storytellershowcaseapp.ui.features.main.MainViewModel
import com.storyteller.Storyteller
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var storytellerDelegate: ShowcaseStorytellerDelegate

  private val viewModel: MainViewModel by viewModels()
  private var savedStateSparseArray = SparseArray<SavedState>()

  override fun onCreate(
    savedInstanceState: Bundle?,
  ) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState != null) {
      savedStateSparseArray = savedInstanceState.getSparseParcelableArray(
        SAVED_STATE_CONTAINER_KEY,
      ) ?: savedStateSparseArray
    }

    viewModel.setup()

    val intentData = intent?.data?.toString()
    if (intentData != null && Storyteller.isStorytellerDeepLink(intentData)) {
      Storyteller.openDeepLink(this, intentData)
    }
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
          onCommit = ::onCommit,
          onSaveState = ::onSaveState,
          onLocationChanged = { storytellerDelegate.onLocationChanged { it } },
        )
      }
    }
  }

  override fun onSaveInstanceState(
    outState: Bundle,
  ) {
    super.onSaveInstanceState(outState)
    outState.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, savedStateSparseArray)
  }

  private fun onCommit(
    fragment: Fragment,
    tag: String,
  ): FragmentTransaction.() -> Unit =
    {
      saveAndRetrieveFragment(fragment)
      replace(SAVED_STATE_FRAGMENT_ID, fragment, tag)
    }

  private fun onSaveState(
    fragment: Fragment,
  ) {
    savedStateSparseArray.put(
      SAVED_STATE_FRAGMENT_ID,
      supportFragmentManager.saveFragmentInstanceState(fragment),
    )
  }

  private fun saveAndRetrieveFragment(
    fragment: Fragment,
  ) {
    if (!fragment.isAdded) {
      fragment.setInitialSavedState(savedStateSparseArray[SAVED_STATE_FRAGMENT_ID])
    }
  }

  companion object {
    private const val SAVED_STATE_CONTAINER_KEY = "SAVED_STATE_CONTAINER_KEY"

    // Moments joaat checksum
    const val SAVED_STATE_FRAGMENT_ID = 96925416
  }
}
