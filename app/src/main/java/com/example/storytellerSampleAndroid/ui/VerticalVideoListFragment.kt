package com.example.storytellerSampleAndroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.storytellerSampleAndroid.databinding.FragmentVerticalVideoBinding
import com.example.storytellerSampleAndroid.models.adapter.MultipleListsAdapter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class VerticalVideoListFragment : Fragment() {


  private val viewModel: VerticalVideoListViewModel by viewModels()

  private val adapter = MultipleListsAdapter()

  private var _binding: FragmentVerticalVideoBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentVerticalVideoBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.recyclerView.adapter = adapter

    viewModel.uiStateFlow
      .onEach { state ->
        binding.refreshLayout.isRefreshing = false
        adapter.data = state.items
      }
      .catch { binding.refreshLayout.isRefreshing = false }
      .launchIn(lifecycleScope)

    binding.refreshLayout.setOnRefreshListener {
      binding.refreshLayout.isRefreshing = true
      viewModel.reloadData()
    }

    binding.refreshLayout.isRefreshing = true
    viewModel.reloadData()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}