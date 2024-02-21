package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.getstoryteller.storytellershowcaseapp.databinding.ListClipGridBinding
import com.getstoryteller.storytellershowcaseapp.databinding.ListClipRowBinding
import com.getstoryteller.storytellershowcaseapp.databinding.ListClipSingletonBinding
import com.getstoryteller.storytellershowcaseapp.databinding.ListStoryGridBinding
import com.getstoryteller.storytellershowcaseapp.databinding.ListStoryRowBinding
import com.getstoryteller.storytellershowcaseapp.databinding.ListStorySingletonBinding
import com.storyteller.ui.list.StorytellerClipsView
import com.storyteller.ui.list.StorytellerStoriesView
import kotlinx.serialization.json.Json.Default.configuration
import kotlin.math.roundToInt

fun Int.dpToPx(
  context: Context,
): Int {
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)
    .roundToInt()
}

abstract class DemoElementViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

  abstract fun bind(
    uiElement: UiElement,
  )
}

class StoryRowViewHolder(private val binding: ListStoryRowBinding) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): StoryRowViewHolder {
      val binding = ListStoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StoryRowViewHolder(binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
  ) {
    val storyRow = uiElement as UiElement.StoryRow
    binding.title.apply {
      isVisible = storyRow.title.isNotEmpty()
      text = storyRow.title
    }
    binding.row.apply {
      val heightResolved = storyRow.height.dpToPx(context)
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = heightResolved
      }
      storyRow.onFailure?.also {
        delegate = StorytellerViewDelegate(storyRow.id, it)
      }

      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyRow.categories,
        cellType = storyRow.cellType,
      )
      if (storyRow.forceDataReload) {
        reloadData()
        storyRow.forceDataReload = false
      }
    }
  }
}

class StoryGridViewHolder(private val binding: ListStoryGridBinding) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): StoryGridViewHolder {
      val binding = ListStoryGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StoryGridViewHolder(binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
  ) {
    val storyGrid = uiElement as UiElement.StoryGrid
    binding.title.apply {
      isVisible = storyGrid.title.isNotEmpty()
      text = storyGrid.title
    }
    binding.grid.run {
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyGrid.categories,
        cellType = storyGrid.cellType,
        displayLimit = storyGrid.displayLimit,
      )

      if (storyGrid.forceDataReload) {
        reloadData()
        storyGrid.forceDataReload = false
      }
    }
  }
}

class ClipGridViewHolder(private val binding: ListClipGridBinding) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): ClipGridViewHolder {
      val binding = ListClipGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipGridViewHolder(binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
  ) {
    val clipsGrid = uiElement as UiElement.ClipGrid
    binding.title.apply {
      isVisible = clipsGrid.title.isNotEmpty()
      text = clipsGrid.title
    }
    binding.grid.run {
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsGrid.collection,
        cellType = clipsGrid.cellType,
        displayLimit = clipsGrid.displayLimit,
      )
      clipsGrid.onFailure?.also {
        delegate = StorytellerViewDelegate(clipsGrid.id, it)
      }
      if (clipsGrid.forceDataReload) {
        reloadData()
        clipsGrid.forceDataReload = false
      }
    }
  }
}

class ClipRowViewHolder(private val binding: ListClipRowBinding) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): ClipRowViewHolder {
      val binding = ListClipRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipRowViewHolder(binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
  ) {
    val clipsRow = uiElement as UiElement.ClipRow
    binding.title.apply {
      isVisible = clipsRow.title.isNotEmpty()
      text = clipsRow.title
    }
    binding.row.run {
      val heightResolved = clipsRow.height.dpToPx(context)
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = heightResolved
      }
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsRow.collection,
        cellType = clipsRow.cellType,
      )
      clipsRow.onFailure?.also {
        delegate = StorytellerViewDelegate(clipsRow.id, it)
      }
      if (clipsRow.forceDataReload) {
        reloadData()
        clipsRow.forceDataReload = false
      }
    }
  }
}

class StorySingletonViewHolder(private val binding: ListStorySingletonBinding) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): StorySingletonViewHolder {
      val binding = ListStorySingletonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StorySingletonViewHolder(binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
  ) {
    val storyRow = uiElement as UiElement.StorySingleton
    binding.title.apply {
      isVisible = storyRow.title.isNotEmpty()
      text = storyRow.title
    }
    binding.row.run {
      storyRow.onFailure?.also {
        delegate = StorytellerViewDelegate(storyRow.id, it)
      }

      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyRow.categories,
        cellType = storyRow.cellType,
        displayLimit = 1,
      )
      if (storyRow.forceDataReload) {
        reloadData()
        storyRow.forceDataReload = false
      }
      post {
        findViewHolderForAdapterPosition(0)?.itemView?.updateLayoutParams<ViewGroup.LayoutParams> {
          width = context.resources.displayMetrics.widthPixels - 20.dpToPx(context).times(2)
          height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
      }
    }
  }
}

class ClipSingletonViewHolder(private val binding: ListClipSingletonBinding) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): ClipSingletonViewHolder {
      val binding = ListClipSingletonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipSingletonViewHolder(binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
  ) {
    val clipsRow = uiElement as UiElement.ClipSingleton
    binding.title.apply {
      isVisible = clipsRow.title.isNotEmpty()
      text = clipsRow.title
    }
    binding.row.run {
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsRow.collection,
        cellType = clipsRow.cellType,
        displayLimit = 1,
      )
      clipsRow.onFailure?.also {
        delegate = StorytellerViewDelegate(clipsRow.id, it)
      }
      if (clipsRow.forceDataReload) {
        reloadData()
        clipsRow.forceDataReload = false
      }
      post {
        findViewHolderForAdapterPosition(0)?.itemView?.updateLayoutParams<ViewGroup.LayoutParams> {
          width = context.resources.displayMetrics.widthPixels - 20.dpToPx(context).times(2)
          height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
      }
    }
  }
}
