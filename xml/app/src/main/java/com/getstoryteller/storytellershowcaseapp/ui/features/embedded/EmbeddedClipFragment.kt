package com.getstoryteller.storytellershowcaseapp.ui.features.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.databinding.FragmentEmbeddedClipBinding
import com.getstoryteller.storytellershowcaseapp.ui.utils.observeOnState
import com.storyteller.ui.pager.StorytellerClipsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmbeddedClipFragment : Fragment() {

  private var nullableBinding: FragmentEmbeddedClipBinding? = null
  private val binding get() = nullableBinding!!

  private val viewModel by viewModels<EmbeddedViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    nullableBinding = FragmentEmbeddedClipBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)
    setInsets()
    observeState()
    viewModel.loadCollection()
  }

  private fun setInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
      val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
      binding.fragmentHost.updatePadding(top = statusBar.top)
      insets
    }
  }

  private fun observeState() {
    observeOnState(state = Lifecycle.State.STARTED) {
      viewModel.state.collect { state ->
        if (state.collection.isEmpty()) {
          binding.emptyState.isVisible = true
        } else {
          binding.emptyState.isVisible = false
          childFragmentManager.beginTransaction().apply {
            val fragment = StorytellerClipsFragment.create(collectionId = state.collection)
            replace(R.id.fragment_host, fragment)
            disallowAddToBackStack()
            commit()
          }
        }
      }
    }
  }
}
