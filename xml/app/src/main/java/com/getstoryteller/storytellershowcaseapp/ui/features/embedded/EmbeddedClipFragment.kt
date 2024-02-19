package com.getstoryteller.storytellershowcaseapp.ui.features.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.getstoryteller.storytellershowcaseapp.R
import com.getstoryteller.storytellershowcaseapp.ShowcaseApp.Companion.CLIP_COLLECTION
import com.getstoryteller.storytellershowcaseapp.databinding.FragmentEmbeddedClipBinding
import com.storyteller.ui.pager.StorytellerClipsFragment

class EmbeddedClipFragment : Fragment() {

  private var nullableBinding: FragmentEmbeddedClipBinding? = null
  private val binding get() = nullableBinding!!

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
    childFragmentManager.beginTransaction().apply {
      val fragment = StorytellerClipsFragment.create(collectionId = CLIP_COLLECTION)
      replace(R.id.fragment_host, fragment)
      disallowAddToBackStack()
      commit()
    }
  }

  private fun setInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
      val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
      binding.fragmentHost.updatePadding(top = statusBar.top)
      insets
    }
  }
}
