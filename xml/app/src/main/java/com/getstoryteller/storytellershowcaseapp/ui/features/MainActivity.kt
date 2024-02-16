package com.getstoryteller.storytellershowcaseapp.ui.features

import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat.getSparseParcelableArray
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
      savedStateSparseArray = getSparseParcelableArray(
        // in =
        savedInstanceState,
        // key =
        SAVED_STATE_CONTAINER_KEY,
        // clazz =
        SavedState::class.java,
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
      storytellerDelegate.onInterceptNavigation {
        val uriToPass = Uri.encode(it)
        navController.navigate("home/link/$uriToPass")
      }
      ShowcaseAppTheme {
        MainScreen(
          activity = this,
          navController = navController,
          viewModel = viewModel,
          onCommit = ::onCommit,
          onSaveInstanceState = { fragmentSaveState(it) },
        )
      }
    }
  }

  private fun FragmentTransaction.fragmentSaveState(
    fragment: Fragment,
  ) {
    val currentFragment = supportFragmentManager.findFragmentByTag(fragment.tag)
    if (currentFragment != null) {
      val savedState = supportFragmentManager.saveFragmentInstanceState(currentFragment)
      savedStateSparseArray.put(currentFragment.id, savedState)
    }
    remove(fragment)
  }

  private fun onCommit(
    fragment: Fragment,
    tag: String,
  ): FragmentTransaction.(containerId: Int) -> Unit =
    { id ->
      saveAndRetrieveFragment(id, fragment)
      replace(id, fragment, tag)
    }

  private fun saveAndRetrieveFragment(
    id: Int,
    fragment: Fragment,
  ) {
    fragment.setInitialSavedState(savedStateSparseArray[id])
  }

  companion object {
    private const val SAVED_STATE_CONTAINER_KEY = "SAVED_STATE_CONTAINER_KEY"
  }
}
