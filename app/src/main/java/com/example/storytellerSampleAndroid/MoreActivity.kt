package com.example.storytellerSampleAndroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.storytellerSampleAndroid.SampleApp.Companion.initializeStoryteller
import com.example.storytellerSampleAndroid.databinding.ActivityMainBinding
import com.example.storytellerSampleAndroid.databinding.ActivityMoreBinding
import com.example.storytellerSampleAndroid.ui.VerticalVideoListFragment
import com.example.storytellerSampleAndroid.ui.VerticalVideoListViewModel
import com.storyteller.Storyteller
import com.storyteller.Storyteller.Companion.activityReentered
import com.storyteller.ui.list.StorytellerClipsView
import com.storyteller.ui.list.StorytellerStoriesView
import com.storyteller.ui.pager.StorytellerClipsFragment
import java.util.UUID

class MoreActivity : AppCompatActivity(R.layout.activity_more) {

  private val title by lazy { intent.getStringExtra(EXTRA_TITLE).orEmpty() }

  private val collection by lazy { intent.getStringExtra(EXTRA_COLLECTION).orEmpty() }

  private val categories by lazy { intent.getStringArrayExtra(EXTRA_CATEGORIES).orEmpty() }

  private lateinit var binding: ActivityMoreBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //make layout stable during system UI changes like hiding/showing status bar
    window.decorView.systemUiVisibility =
      window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    binding = ActivityMoreBinding.inflate(layoutInflater)

    setContentView(binding.root)

    setSupportActionBar(binding.moreToolbar)
    binding.moreTitle.text = title
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    if (categories.isNotEmpty()) {
      binding.storiesGridView.isVisible = true
      binding.clipsGridView.isVisible = false

      binding.storiesGridView.configuration = StorytellerStoriesView.ListConfiguration(
        categories = categories.toList()
      )
      binding.storiesGridView.reloadData()
    }

    if (collection.isNotBlank()) {
      binding.storiesGridView.isVisible = false
      binding.clipsGridView.isVisible = true

      binding.clipsGridView.configuration = StorytellerClipsView.ListConfiguration(
        collection = collection
      )
      binding.clipsGridView.reloadData()
    }

  }

  override fun onActivityReenter(resultCode: Int, data: Intent?) {
    super.onActivityReenter(resultCode, data)
    // This method allows smooth close transition syncing. It should be used inside `onActivityReenter` only.
    activityReentered()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == android.R.id.home) {
      onBackPressedDispatcher.onBackPressed()
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  companion object {

    private const val EXTRA_COLLECTION = "collection"
    private const val EXTRA_TITLE = "title"
    private const val EXTRA_CATEGORIES = "categories"
    fun start(context: Context, title: String, categories: List<String>) {
      val intent = Intent(context, MoreActivity::class.java).apply {
        putExtra(EXTRA_TITLE, title)
        putExtra(EXTRA_CATEGORIES, categories.toTypedArray())
      }
      context.startActivity(intent)
    }

    fun start(context: Context, title: String, collection: String) {
      val intent = Intent(context, MoreActivity::class.java).apply {
        putExtra(EXTRA_TITLE, title)
        putExtra(EXTRA_COLLECTION, collection)
      }
      context.startActivity(intent)
    }
  }
}
