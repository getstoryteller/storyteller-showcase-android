package com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MultipleListsAdapter(
  data: List<UiElement> = listOf(),
  private val onRemoteItemAction: (String) -> Unit,
  private val onClickMore: (title: String, categories: List<String>, collection: String) -> Unit,
) : RecyclerView.Adapter<DemoElementViewHolder>() {

  var data: List<UiElement> = data
    set(value) {
      calculateDataDiff(value, data).dispatchUpdatesTo(this)
      field = value
    }

  companion object {
    private val supportedTypes = listOf<Class<*>>(
      UiElement.StoryRow::class.java,
      UiElement.StoryGrid::class.java,
      UiElement.StorySingleton::class.java,
      UiElement.ClipRow::class.java,
      UiElement.ClipGrid::class.java,
      UiElement.ClipSingleton::class.java,
    )
    private val supportedInflaters = listOf(
      StoryRowViewHolder.Companion::inflate,
      StoryGridViewHolder.Companion::inflate,
      StorySingletonViewHolder.Companion::inflate,
      ClipRowViewHolder.Companion::inflate,
      ClipGridViewHolder.Companion::inflate,
      ClipSingletonViewHolder.Companion::inflate,
    )

    fun calculateDataDiff(
      newData: List<UiElement>,
      oldData: List<UiElement>,
    ) = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
      override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
      ) = newData[newItemPosition].id == oldData[oldItemPosition].id

      override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
      ) = newData[newItemPosition] == oldData[oldItemPosition]

      override fun getOldListSize() = oldData.size
      override fun getNewListSize() = newData.size
    })
  }

  override fun getItemViewType(
    position: Int,
  ): Int {
    val element = data[position]
    val index = supportedTypes.indexOf(element.javaClass)
    require(index >= 0) { "Element type is not supported" }
    return index
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): DemoElementViewHolder {
    return supportedInflaters[viewType](parent)
  }

  override fun onBindViewHolder(
    holder: DemoElementViewHolder,
    position: Int,
  ) {
    val element = data[position]
    holder.bind(element, onRemoteItemAction, onClickMore)
    return
  }

  override fun getItemCount(): Int {
    return data.size
  }
}
