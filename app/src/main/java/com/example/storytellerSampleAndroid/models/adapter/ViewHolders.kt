package com.example.storytellerSampleAndroid.models.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.storytellerSampleAndroid.MoreActivity
import com.example.storytellerSampleAndroid.databinding.ListClipGridBinding
import com.example.storytellerSampleAndroid.databinding.ListClipRowBinding
import com.example.storytellerSampleAndroid.databinding.ListStoryGridBinding
import com.example.storytellerSampleAndroid.databinding.ListStoryRowBinding
import com.example.storytellerSampleAndroid.models.ClipsGridItem
import com.example.storytellerSampleAndroid.models.ClipsRowItem
import com.example.storytellerSampleAndroid.models.ClipsSingletonItem
import com.example.storytellerSampleAndroid.models.Item
import com.example.storytellerSampleAndroid.models.StoriesGridItem
import com.example.storytellerSampleAndroid.models.StoriesRowItem
import com.example.storytellerSampleAndroid.models.StoriesSingletonItem
import com.example.storytellerSampleAndroid.theme.StorytellerThemes
import com.storyteller.domain.entities.Error
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.ui.list.StorytellerClipsView
import com.storyteller.ui.list.StorytellerListViewDelegate
import com.storyteller.ui.list.StorytellerStoriesView
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun Int.dpToPx(context: Context): Int {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
  ).roundToInt()
}

interface RemoveItemDelegate: StorytellerListViewDelegate{
  override fun onDataLoadStarted() = Unit
  override fun onPlayerDismissed() = Unit
  override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int)
}

abstract class UiItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

  val lifecycleScope
    get() = view.findViewTreeLifecycleOwner()?.lifecycleScope
  open fun bind(uiElement: Item) {
  }
}

class StoryRowViewHolder(private val binding: ListStoryRowBinding) :
  UiItemViewHolder(binding.root) {
  companion object {
    fun inflate(parent: ViewGroup): StoryRowViewHolder {
      val binding = ListStoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StoryRowViewHolder(binding)
    }
  }

  override fun bind(uiElement: Item) {
    super.bind(uiElement)
    val storyRow = uiElement as StoriesRowItem
    binding.moreButton.isVisible = storyRow.moreButtonTitle.isNotBlank()
    binding.moreButton.text = storyRow.moreButtonTitle
    binding.moreButton.setOnClickListener {
      MoreActivity.start(
        context = view.context,
        title = storyRow.title,
        categories = storyRow.categories
      )
    }
    binding.titleTextView.isVisible = storyRow.title.isNotBlank()
    binding.titleTextView.text = storyRow.title
    binding.storytellerRow.apply {

      delegate = object : RemoveItemDelegate{
        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          if (!success || dataCount == 0){
            lifecycleScope?.launch {
              storyRow.removeItemFlow?.emit(storyRow.id)
            }
          }
        }
      }
      val heightResolved = storyRow.heightDp.dpToPx(context)
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = heightResolved
     }

      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyRow.categories,
        cellType = storyRow.cellType,
        // set different theme than the Global one in SampleApp
        displayLimit = storyRow.count,
        theme = if(storyRow.cellType == StorytellerListViewCellType.ROUND){
          StorytellerThemes.getRoundTheme(context)
        }else{
          StorytellerThemes.getGlobalTheme(context)
        }
      )
      if (storyRow.forceReload) {
        reloadData()
        storyRow.forceReload = false
      }
    }
  }
}

class StoryGridViewHolder(private val binding: ListStoryGridBinding) :
  UiItemViewHolder(binding.root) {
  companion object {
    fun inflate(parent: ViewGroup): StoryGridViewHolder {
      val binding = ListStoryGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StoryGridViewHolder(binding)
    }
  }

  override fun bind(uiElement: Item) {
    super.bind(uiElement)
    val storyGrid = uiElement as StoriesGridItem
    binding.moreButton.isVisible = storyGrid.moreButtonTitle.isNotBlank()
    binding.moreButton.text = storyGrid.moreButtonTitle
    binding.moreButton.setOnClickListener {
      MoreActivity.start(
        context = view.context,
        title = storyGrid.title,
        categories = storyGrid.categories
      )
    }
    binding.titleTextView.isVisible = storyGrid.title.isNotBlank()
    binding.titleTextView.text = storyGrid.title
    binding.storytellerGrid.apply {
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyGrid.categories,
        cellType = storyGrid.cellType,
        displayLimit = storyGrid.count
      )
      delegate = object : RemoveItemDelegate{
        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          if (!success || dataCount == 0){
            lifecycleScope?.launch {
              storyGrid.removeItemFlow?.emit(storyGrid.id)
            }

          }
        }
      }
      if (storyGrid.forceReload) {
        reloadData()
        storyGrid.forceReload = false
      }
    }
  }
}

class ClipGridViewHolder(private val binding: ListClipGridBinding) :
  UiItemViewHolder(binding.root) {
  companion object {
    fun inflate(parent: ViewGroup): ClipGridViewHolder {
      val binding = ListClipGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipGridViewHolder(binding)
    }
  }

  override fun bind(uiElement: Item) {
    super.bind(uiElement)
    val clipsGrid = uiElement as ClipsGridItem
    binding.moreButton.isVisible = clipsGrid.moreButtonTitle.isNotBlank()
    binding.moreButton.text = clipsGrid.moreButtonTitle
    binding.moreButton.setOnClickListener {
      MoreActivity.start(
        context = view.context,
        title = clipsGrid.title,
        collection = clipsGrid.collection
      )
    }
    binding.titleTextView.isVisible = clipsGrid.title.isNotBlank()
    binding.titleTextView.text = clipsGrid.title
    binding.storytellerClipGrid.apply {
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsGrid.collection,
        cellType = clipsGrid.cellType,
        // set different theme than the Global one in SampleApp
        displayLimit = clipsGrid.count
      )
      delegate = object : RemoveItemDelegate{
        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          if (!success || dataCount == 0){
            lifecycleScope?.launch {
              clipsGrid.removeItemFlow?.emit(clipsGrid.id)
            }
          }
        }
      }

      if (clipsGrid.forceReload) {
        reloadData()
        clipsGrid.forceReload = false
      }
    }
  }
}

class ClipRowViewHolder(private val binding: ListClipRowBinding) :
  UiItemViewHolder(binding.root) {
  companion object {
    fun inflate(parent: ViewGroup): ClipRowViewHolder {
      val binding = ListClipRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipRowViewHolder(binding)
    }
  }

  override fun bind(uiElement: Item) {
    super.bind(uiElement)
    val clipsRow = uiElement as ClipsRowItem
    binding.moreButton.isVisible = clipsRow.moreButtonTitle.isNotBlank()
    binding.moreButton.text = clipsRow.moreButtonTitle
    binding.moreButton.setOnClickListener {
      MoreActivity.start(
        context = view.context,
        title = clipsRow.title,
        collection = clipsRow.collection
      )
    }
    binding.titleTextView.isVisible = clipsRow.title.isNotBlank()
    binding.titleTextView.text = clipsRow.title
    binding.storytellerClipRow.apply {
      val heightResolved = clipsRow.heightDp.dpToPx(context)
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = heightResolved
      }

      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsRow.collection,
        cellType = clipsRow.cellType,
        displayLimit = clipsRow.count
      )
      delegate = object : RemoveItemDelegate{
        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          if (!success || dataCount == 0){
            lifecycleScope?.launch {
              clipsRow.removeItemFlow?.emit(clipsRow.id)
            }
          }
        }
      }
      if (clipsRow.forceReload) {
        reloadData()
        clipsRow.forceReload = false
      }
    }
  }
}

class ClipSingletonViewHolder(private val binding: ListClipGridBinding) :
  UiItemViewHolder(binding.root) {
  companion object {
    fun inflate(parent: ViewGroup): ClipSingletonViewHolder {
      val binding = ListClipGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipSingletonViewHolder(binding)
    }
  }

  override fun bind(uiElement: Item) {
    super.bind(uiElement)
    val clipsGrid = uiElement as ClipsSingletonItem
    binding.moreButton.isVisible = clipsGrid.moreButtonTitle.isNotBlank()
    binding.moreButton.text = clipsGrid.moreButtonTitle
    binding.moreButton.setOnClickListener {
      MoreActivity.start(
        context = view.context,
        title = clipsGrid.title,
        collection = clipsGrid.collection
      )
    }
    binding.titleTextView.isVisible = clipsGrid.title.isNotBlank()
    binding.titleTextView.text = clipsGrid.title
    binding.storytellerClipGrid.apply {
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsGrid.collection,
        theme = StorytellerThemes.getSingletonTheme(context),
        displayLimit = 1
      )
      delegate = object : RemoveItemDelegate{
        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          if (!success || dataCount == 0){
            lifecycleScope?.launch {
              clipsGrid.removeItemFlow?.emit(clipsGrid.id)
            }
          }
        }
      }
      if (clipsGrid.forceReload) {
        reloadData()
        clipsGrid.forceReload = false
      }
    }
  }
}

class StorySingletonViewHolder(private val binding: ListStoryGridBinding) :
  UiItemViewHolder(binding.root) {
  companion object {
    fun inflate(parent: ViewGroup): StorySingletonViewHolder {
      val binding = ListStoryGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StorySingletonViewHolder(binding)
    }
  }

  override fun bind(uiElement: Item) {
    super.bind(uiElement)
    val storyGrid = uiElement as StoriesSingletonItem
    binding.moreButton.isVisible = storyGrid.moreButtonTitle.isNotBlank()
    binding.moreButton.text = storyGrid.moreButtonTitle
    binding.moreButton.setOnClickListener {
      MoreActivity.start(
        context = view.context,
        title = storyGrid.title,
        categories = storyGrid.categories
      )
    }
    binding.titleTextView.isVisible = storyGrid.title.isNotBlank()
    binding.titleTextView.text = storyGrid.title
    binding.storytellerGrid.apply {
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyGrid.categories,
        // set different theme than the Global one in SampleApp
        theme = StorytellerThemes.getSingletonTheme(context),
        displayLimit = 1
      )
      delegate = object : RemoveItemDelegate{
        override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
          if (!success || dataCount == 0){
            lifecycleScope?.launch {
              storyGrid.removeItemFlow?.emit(storyGrid.id)
            }
          }
        }
      }

      if (storyGrid.forceReload) {
        reloadData()
        storyGrid.forceReload = false
      }
    }
  }
}


