package com.example.storytellerSampleAndroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storytellerSampleAndroid.databinding.FragmentVerticalVideoBinding
import com.example.storytellerSampleAndroid.models.GetVideoListUseCase
import com.example.storytellerSampleAndroid.models.VideoRepo
import com.example.storytellerSampleAndroid.models.adapter.MultipleListsAdapter
import com.example.storytellerSampleAndroid.preferences.SharedPreferencesManager
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class VerticalVideoListFragment : Fragment() {


  lateinit var viewModel: VerticalVideoListViewModel

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

    val useCase = GetVideoListUseCase(
      sharedPreferencesManager = SharedPreferencesManager(requireContext())
    )
    viewModel = ViewModelProvider(
      this,
      VerticalVideoListViewModel.Factory(useCase)
    )[VerticalVideoListViewModel::class.java]

    viewModel.uiStateFlow
      .onEach { state ->
        binding.refreshLayout.isRefreshing = false
        adapter.data = state.items
      }
      .catch { binding.refreshLayout.isRefreshing = false }
      .launchIn(lifecycleScope)

    binding.refreshLayout.setOnRefreshListener {
      reloadData()
    }

    reloadData()
  }

  fun reloadData() {
    binding.refreshLayout.isRefreshing = true
    viewModel.reloadData()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

}