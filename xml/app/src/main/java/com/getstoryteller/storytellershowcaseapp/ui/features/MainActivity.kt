package com.getstoryteller.storytellershowcaseapp.ui.features

import android.os.Bundle
import android.util.SparseArray
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat.getSparseParcelableArray
import androidx.fragment.app.Fragment.SavedState
import com.getstoryteller.storytellershowcaseapp.R
import com.storyteller.Storyteller
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

  companion object {
    private const val SAVED_STATE_CONTAINER_KEY = "SAVED_STATE_CONTAINER_KEY"
  }
}
