package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  )
}

class StoryRowViewHolder(
  private val parent: ViewGroup,
  private val binding: ListStoryRowBinding,
) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): StoryRowViewHolder {
      val binding = ListStoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StoryRowViewHolder(parent, binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  ) {
    val storyRow = uiElement as UiElement.StoryRow
    binding.more.apply {
      isVisible = storyRow.more.isNotEmpty()
      text = storyRow.more
      setOnClickListener {
        onClickMore.invoke(storyRow.title, storyRow.categories, "")
      }
    }
    binding.title.apply {
      isVisible = storyRow.title.isNotEmpty()
      text = storyRow.title
    }
    binding.row.apply {
      setViewHolderParent(this@StoryRowViewHolder.parent)
      val heightResolved = storyRow.height.dpToPx(context)
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = heightResolved
      }
      delegate = StorytellerViewDelegate(storyRow.id, onRemoteItemAction)
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyRow.categories,
        cellType = storyRow.cellType,
        theme = storyRow.theme,
      )
      if (storyRow.forceDataReload) {
        reloadData()
        storyRow.forceDataReload = false
      }
    }
  }
}

class StoryGridViewHolder(
  private val parent: ViewGroup,
  private val binding: ListStoryGridBinding,
) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): StoryGridViewHolder {
      val binding = ListStoryGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StoryGridViewHolder(parent, binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  ) {
    val storyGrid = uiElement as UiElement.StoryGrid
    binding.more.apply {
      isVisible = storyGrid.more.isNotEmpty()
      text = storyGrid.more
      setOnClickListener {
        onClickMore.invoke(storyGrid.title, storyGrid.categories, "")
      }
    }
    binding.title.apply {
      isVisible = storyGrid.title.isNotEmpty()
      text = storyGrid.title
    }
    binding.grid.run {
      setViewHolderParent(this@StoryGridViewHolder.parent)
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyGrid.categories,
        cellType = storyGrid.cellType,
        displayLimit = storyGrid.displayLimit,
        theme = storyGrid.theme,
      )
      delegate = StorytellerViewDelegate(storyGrid.id, onRemoteItemAction)
      if (storyGrid.forceDataReload) {
        reloadData()
        storyGrid.forceDataReload = false
      }
    }
  }
}

class ClipGridViewHolder(
  private val parent: ViewGroup,
  private val binding: ListClipGridBinding,
) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): ClipGridViewHolder {
      val binding = ListClipGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipGridViewHolder(parent, binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  ) {
    val clipsGrid = uiElement as UiElement.ClipGrid
    binding.more.apply {
      isVisible = clipsGrid.more.isNotEmpty()
      text = clipsGrid.more
      setOnClickListener {
        onClickMore.invoke(clipsGrid.title, emptyList(), clipsGrid.collection)
      }
    }
    binding.title.apply {
      isVisible = clipsGrid.title.isNotEmpty()
      text = clipsGrid.title
    }
    binding.grid.run {
      setViewHolderParent(this@ClipGridViewHolder.parent)
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsGrid.collection,
        cellType = clipsGrid.cellType,
        displayLimit = clipsGrid.displayLimit,
        theme = clipsGrid.theme,
      )
      delegate = StorytellerViewDelegate(clipsGrid.id, onRemoteItemAction)
      if (clipsGrid.forceDataReload) {
        reloadData()
        clipsGrid.forceDataReload = false
      }
    }
  }
}

class ClipRowViewHolder(
  private val parent: ViewGroup,
  private val binding: ListClipRowBinding,
) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): ClipRowViewHolder {
      val binding = ListClipRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipRowViewHolder(parent, binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  ) {
    val clipsRow = uiElement as UiElement.ClipRow
    binding.more.apply {
      isVisible = clipsRow.more.isNotEmpty()
      text = clipsRow.more
      setOnClickListener {
        onClickMore.invoke(clipsRow.title, emptyList(), clipsRow.collection)
      }
    }
    binding.title.apply {
      isVisible = clipsRow.title.isNotEmpty()
      text = clipsRow.title
    }
    binding.row.run {
      setViewHolderParent(this@ClipRowViewHolder.parent)
      val heightResolved = clipsRow.height.dpToPx(context)
      updateLayoutParams<ViewGroup.LayoutParams> {
        height = heightResolved
      }
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsRow.collection,
        cellType = clipsRow.cellType,
        theme = clipsRow.theme,
      )
      delegate = StorytellerViewDelegate(clipsRow.id, onRemoteItemAction)
      if (clipsRow.forceDataReload) {
        reloadData()
        clipsRow.forceDataReload = false
      }
    }
  }
}

class StorySingletonViewHolder(
  private val parent: ViewGroup,
  private val binding: ListStorySingletonBinding,
) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): StorySingletonViewHolder {
      val binding = ListStorySingletonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return StorySingletonViewHolder(parent, binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  ) {
    val storyRow = uiElement as UiElement.StorySingleton
    binding.more.apply {
      isVisible = storyRow.more.isNotEmpty()
      text = storyRow.more
      setOnClickListener {
        onClickMore.invoke(storyRow.title, storyRow.categories, "")
      }
    }
    binding.title.apply {
      isVisible = storyRow.title.isNotEmpty()
      text = storyRow.title
    }
    binding.row.run {
      setViewHolderParent(this@StorySingletonViewHolder.parent)
      delegate = StorytellerViewDelegate(storyRow.id, onRemoteItemAction)
      configuration = StorytellerStoriesView.ListConfiguration(
        categories = storyRow.categories,
        cellType = storyRow.cellType,
        displayLimit = 1,
        theme = storyRow.theme?.let {
          it.copy(
            light = it.light.copy(
              lists = it.light.lists.copy(
                grid = it.light.lists.grid.copy(
                  topInset = 0,
                  columns = 1,
                ),
              ),
              tiles = it.light.tiles.copy(
                title = it.light.tiles.title.copy(
                  titleSize = 28,
                  alignment = Gravity.START,
                  lineHeight = 28,
                ),
              ),
            ),
            dark = it.dark.copy(
              lists = it.dark.lists.copy(
                grid = it.dark.lists.grid.copy(
                  topInset = 0,
                  columns = 1,
                ),
              ),
              tiles = it.dark.tiles.copy(
                title = it.dark.tiles.title.copy(
                  titleSize = 28,
                  alignment = Gravity.START,
                  lineHeight = 28,
                ),
              ),
            ),
          )
        },
      )
      if (storyRow.forceDataReload) {
        reloadData()
        storyRow.forceDataReload = false
      }
    }
  }
}

class ClipSingletonViewHolder(
  private val parent: ViewGroup,
  private val binding: ListClipSingletonBinding,
) : DemoElementViewHolder(binding.root) {
  companion object {
    fun inflate(
      parent: ViewGroup,
    ): ClipSingletonViewHolder {
      val binding = ListClipSingletonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ClipSingletonViewHolder(parent, binding)
    }
  }

  override fun bind(
    uiElement: UiElement,
    onRemoteItemAction: (String) -> Unit,
    onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
  ) {
    val clipsRow = uiElement as UiElement.ClipSingleton
    binding.more.apply {
      isVisible = clipsRow.more.isNotEmpty()
      text = clipsRow.more
      setOnClickListener {
        onClickMore.invoke(clipsRow.title, emptyList(), clipsRow.collection)
      }
    }
    binding.title.apply {
      isVisible = clipsRow.title.isNotEmpty()
      text = clipsRow.title
    }
    binding.row.run {
      setViewHolderParent(this@ClipSingletonViewHolder.parent)
      configuration = StorytellerClipsView.ListConfiguration(
        collection = clipsRow.collection,
        cellType = clipsRow.cellType,
        displayLimit = 1,
        theme = clipsRow.theme?.let {
          it.copy(
            light = it.light.copy(
              lists = it.light.lists.copy(
                grid = it.light.lists.grid.copy(
                  topInset = 0,
                  columns = 1,
                ),
              ),
              tiles = it.light.tiles.copy(
                title = it.light.tiles.title.copy(
                  titleSize = 28,
                  alignment = Gravity.START,
                  lineHeight = 28,
                ),
              ),
            ),
            dark = it.dark.copy(
              lists = it.dark.lists.copy(
                grid = it.dark.lists.grid.copy(
                  topInset = 0,
                  columns = 1,
                ),
              ),
              tiles = it.dark.tiles.copy(
                title = it.dark.tiles.title.copy(
                  titleSize = 28,
                  alignment = Gravity.START,
                  lineHeight = 28,
                ),
              ),
            ),
          )
        },
      )
      delegate = StorytellerViewDelegate(clipsRow.id, onRemoteItemAction)
      if (clipsRow.forceDataReload) {
        reloadData()
        clipsRow.forceDataReload = false
      }
    }
  }
}
