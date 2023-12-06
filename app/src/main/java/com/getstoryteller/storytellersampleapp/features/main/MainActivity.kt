package com.getstoryteller.storytellersampleapp.features.main

import android.os.Bundle
import android.util.SparseArray
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.Fragment.SavedState
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.compose.rememberNavController
import com.getstoryteller.storytellersampleapp.ui.SampleAppTheme
import com.storyteller.ui.pager.StorytellerClipsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by viewModels()
  private var savedStateSparseArray = SparseArray<SavedState>()
  private var currentSelectItemId = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState != null) {
      savedStateSparseArray = BundleCompat.getSparseParcelableArray(
        /* in = */ savedInstanceState,
        /* key = */ SAVED_STATE_CONTAINER_KEY,
        /* clazz = */ SavedState::class.java,
      ) ?: savedStateSparseArray
      currentSelectItemId = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY)
    }

    viewModel.setup()
    setContent {
      val navController = rememberNavController()
      SampleAppTheme {
        Surface(
          modifier = Modifier.background(color = MaterialTheme.colors.surface),
        ) {
          MainScreen(
            activity = this,
            navController = navController,
            viewModel = viewModel,
            onCommit = ::onCommit,
            getClipsFragment = ::getStorytellerClipsFragment,
          )
        }
      }
    }
  }

  private fun getStorytellerClipsFragment(): StorytellerClipsFragment? {
    return supportFragmentManager.fragments.firstOrNull { it is StorytellerClipsFragment } as? StorytellerClipsFragment
  }

  private fun onCommit(
    fragment: Fragment,
    tag: String,
  ): FragmentTransaction.(containerId: Int) -> Unit = { id ->
    saveAndRetrieveFragment(supportFragmentManager, id, fragment)
    replace(id, fragment, tag)
  }

  private fun saveAndRetrieveFragment(
    supportFragmentManager: FragmentManager,
    tabId: Int,
    fragment: Fragment,
  ) {
    val currentFragment = supportFragmentManager.findFragmentById(currentSelectItemId)
    if (currentFragment != null) {
      savedStateSparseArray.put(
        currentSelectItemId,
        supportFragmentManager.saveFragmentInstanceState(currentFragment),
      )
    }
    currentSelectItemId = tabId
    val savedState = savedStateSparseArray[currentSelectItemId]
    fragment.setInitialSavedState(savedState)
  }


  companion object {
    private const val SAVED_STATE_CONTAINER_KEY = "SAVED_STATE_CONTAINER_KEY"
    private const val SAVED_STATE_CURRENT_TAB_KEY = "SAVED_STATE_CURRENT_TAB_KEY"
  }
}
