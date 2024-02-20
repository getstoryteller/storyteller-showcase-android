package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.getstoryteller.storytellershowcaseapp.MainNavigationDirections
import com.getstoryteller.storytellershowcaseapp.databinding.FragmentDashboardBinding
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.DashboardContract.Effect.Logout
import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.MultipleListsAdapter
import com.getstoryteller.storytellershowcaseapp.ui.features.getMainActivityNavigator
import com.getstoryteller.storytellershowcaseapp.ui.utils.observeOnState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

  private var nullableBinding: FragmentDashboardBinding? = null
  private val binding get() = nullableBinding!!

  private val viewModel by viewModels<DashboardViewModel>()

  private val navigation by lazy { getMainActivityNavigator() }

  private val demoAdapter: MultipleListsAdapter = MultipleListsAdapter()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    nullableBinding = FragmentDashboardBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)
    setViews()
    setInsets()
    observeState()
    observeEffects()
    viewModel.reload()
  }

  private fun setViews() {
    binding.refreshLayout.apply {
      setProgressViewOffset(true, 100, 200)
      setOnRefreshListener {
        viewModel.reload()
      }
    }
    binding.multipleListsRecycler.apply {
      adapter = demoAdapter
    }
    binding.logout.setOnClickListener {
      viewModel.logout()
    }
  }

  private fun setInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
      val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
      binding.root.updatePadding(top = statusBar.top)
      insets
    }
  }

  private fun observeState() {
    observeOnState(state = Lifecycle.State.STARTED) {
      viewModel.state.collect { state ->
        binding.refreshLayout.isRefreshing = state.isLoading
        if (state.data.isNotEmpty()) demoAdapter.data = state.data
      }
    }
  }

  private fun observeEffects() {
    observeOnState(state = Lifecycle.State.STARTED) {
      viewModel.effects.collect { effect ->
        when (effect) {
          is Logout -> {
            navigation.navigate(MainNavigationDirections.actionToLogin())
          }
        }
      }
    }
  }
}
