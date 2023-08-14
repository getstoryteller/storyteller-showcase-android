package com.example.storytellerSampleAndroid.models.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storytellerSampleAndroid.models.ClipsGridItem
import com.example.storytellerSampleAndroid.models.ClipsRowItem
import com.example.storytellerSampleAndroid.models.ClipsSingletonItem
import com.example.storytellerSampleAndroid.models.Item
import com.example.storytellerSampleAndroid.models.StoriesGridItem
import com.example.storytellerSampleAndroid.models.StoriesRowItem
import com.example.storytellerSampleAndroid.models.StoriesSingletonItem

class MultipleListsAdapter(
  data: List<Item> = listOf()
) : RecyclerView.Adapter<UiItemViewHolder>() {

  var data: List<Item> = data
    set(value) {
      calculateDataDiff(value, data).dispatchUpdatesTo(this)
      field = value
    }

  companion object {
    private val supportedTypes = listOf<Class<*>>(
      StoriesRowItem::class.java,
      StoriesGridItem::class.java,
      ClipsRowItem::class.java,
      ClipsGridItem::class.java,
      ClipsSingletonItem::class.java,
      StoriesSingletonItem::class.java
    )
    private val supportedInflaters = listOf(
      StoryRowViewHolder.Companion::inflate,
      StoryGridViewHolder.Companion::inflate,
      ClipRowViewHolder.Companion::inflate,
      ClipGridViewHolder.Companion::inflate,
      ClipSingletonViewHolder.Companion::inflate,
      StorySingletonViewHolder.Companion::inflate
    )

    fun calculateDataDiff(newData: List<Item>, oldData: List<Item>) =
      DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          newData[newItemPosition].id == oldData[oldItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
          newData[newItemPosition] == oldData[oldItemPosition]

        override fun getOldListSize() = oldData.size
        override fun getNewListSize() = newData.size
      })
  }

  override fun getItemViewType(position: Int): Int {
    val element = data[position]
    val index = supportedTypes.indexOf(element.javaClass)
    require(index >= 0) { "Element type is not supported" }
    return index
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiItemViewHolder {
    return supportedInflaters[viewType](parent)
  }

  override fun onBindViewHolder(holder: UiItemViewHolder, position: Int) {
    val element = data[position]
    holder.bind(element)
    return
  }

  override fun getItemCount(): Int {
    return data.size
  }
}
