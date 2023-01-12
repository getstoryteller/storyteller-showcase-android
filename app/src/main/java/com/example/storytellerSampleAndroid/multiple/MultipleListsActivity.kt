package com.example.storytellerSampleAndroid.multiple

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storytellerSampleAndroid.R
import com.example.storytellerSampleAndroid.databinding.ActivityMultipleListsBinding
import com.example.storytellerSampleAndroid.multiple.adapter.MultipleListsAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MultipleListsActivity : AppCompatActivity(R.layout.activity_multiple_lists) {

    private lateinit var binding: ActivityMultipleListsBinding
    private val viewModel: MultipleListsViewModel by viewModels()
    private val demoAdapter: MultipleListsAdapter = MultipleListsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //make layout stable during system UI changes like hiding/showing status bar
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding = ActivityMultipleListsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup refresh layout
        binding.refreshLayout.apply {
            setOnRefreshListener {
                /*
                Tell the SDK to load the latest data from the API
                 */
                viewModel.reload()
            }
        }

        binding.multipleListsRecycler.apply {
            adapter = demoAdapter
        }

        viewModel.uiData.onEach {
            demoAdapter.data = it
            binding.refreshLayout.isRefreshing = false
        }.launchIn(lifecycleScope)
    }

}