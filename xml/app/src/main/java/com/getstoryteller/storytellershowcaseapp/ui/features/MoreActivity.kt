package com.getstoryteller.storytellershowcaseapp.ui.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.getstoryteller.storytellershowcaseapp.databinding.ActivityMoreBinding
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.ui.list.StorytellerClipsGridView
import com.storyteller.ui.list.StorytellerClipsView
import com.storyteller.ui.list.StorytellerStoriesGridView
import com.storyteller.ui.list.StorytellerStoriesView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMoreBinding

  private val title: String
    get() = intent.getStringExtra("title").orEmpty()

  private val categories: List<String>
    get() = intent.getStringArrayExtra("categories")?.toList().orEmpty()

  private val collection: String
    get() = intent.getStringExtra("collection").orEmpty()

  override fun onCreate(
    savedInstanceState: Bundle?,
  ) {
    super.onCreate(savedInstanceState)
    binding = ActivityMoreBinding.inflate(layoutInflater)
    enableEdgeToEdge()
    setContentView(binding.root)
    generateStorytellerView(title, categories, collection)
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    binding.toolbar.setNavigationOnClickListener {
      onBackPressedDispatcher.onBackPressed()
    }
  }

  private fun generateStorytellerView(
    title: String,
    categories: List<String>,
    collection: String,
  ) {
    binding.toolbar.title = title
    binding.content.apply {
      if (collection.isEmpty()) {
        generateStorytellerStoriesView(categories)
      } else {
        generateStorytellerClipsView(collection)
      }
    }
  }

  private fun generateStorytellerStoriesView(
    categories: List<String>,
  ) {
    StorytellerStoriesGridView(context = this).apply {
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = categories,
        cellType = StorytellerListViewCellType.SQUARE,
      )
      binding.content.addView(this)
      reloadData()
    }
  }

  private fun generateStorytellerClipsView(
    collection: String,
  ) {
    StorytellerClipsGridView(context = this).apply {
      configuration = StorytellerClipsView.ListConfiguration(
        collection = collection,
        cellType = StorytellerListViewCellType.SQUARE,
      )
      binding.content.addView(this)
      reloadData()
    }
  }

  companion object {
    fun start(
      context: Context?,
      title: String,
      categories: List<String>,
      collection: String,
    ) {
      val intent = createIntent(context, title, categories, collection)
      context?.startActivity(intent)
    }

    private fun createIntent(
      context: Context?,
      title: String,
      categories: List<String>,
      collection: String,
    ): Intent {
      return Intent(context, MoreActivity::class.java).apply {
        putExtra("title", title)
        putExtra("categories", categories.toTypedArray())
        putExtra("collection", collection)
      }
    }
  }
}
