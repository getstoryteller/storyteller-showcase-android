package com.getstoryteller.storytellershowcaseapp.ui.features

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat.getSparseParcelableArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.Fragment.SavedState
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.getstoryteller.storytellershowcaseapp.R
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    val intentData = intent?.data?.toString()
    if (intentData != null && Storyteller.isStorytellerDeepLink(intentData)) {
      Storyteller.openDeepLink(this, intentData)
    }
    enableEdgeToEdge()
    setContentView(R.layout.activity_main)
  }

  override fun onActivityReenter(
    resultCode: Int,
    data: Intent?,
  ) {
    super.onActivityReenter(resultCode, data)
    // This method allows smooth close transition syncing. It should be used inside `onActivityReenter` only.
    activityReentered()
  }

  companion object {
    private const val SAVED_STATE_CONTAINER_KEY = "SAVED_STATE_CONTAINER_KEY"
  }
}

fun Fragment.getMainActivityNavigator() =
  (requireActivity() as MainActivity).findViewById<FragmentContainerView>(R.id.main_nav_host_fragment)
    .findNavController()
