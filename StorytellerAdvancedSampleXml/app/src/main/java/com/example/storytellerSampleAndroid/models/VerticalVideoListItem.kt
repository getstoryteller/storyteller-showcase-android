package com.example.storytellerSampleAndroid.models


import com.storyteller.domain.entities.StorytellerListViewCellType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class Layout {
  @SerialName("singleton") SINGLETON,
  @SerialName("grid") GRID,
  @SerialName("row") ROW

}

@Serializable
enum class VideoType {
  @SerialName("clips") CLIPS,
  @SerialName("stories") STORIES,
}

@Serializable
enum class Size {
  @SerialName("regular") REGULAR,
  @SerialName("medium") MEDIUM,
  @SerialName("large")  LARGE,
}

@Serializable
data class VerticalVideoListDto(val data: List<VerticalVideoListItem>)

@Serializable
data class VerticalVideoListItem(
  val layout: Layout? = null,
  val storyId: String? = null,
  val moreButtonTitle: String? = null,
  val imageUrl: String? = null,
  val count: Int? = null,
  val categories: List<String>? = null,
  val collection: String? = null,
  val size: Size?,
  val tileType: String,
  val title: String? = null,
  val videoType: VideoType,
  val id: String,
)

sealed class Item(open val id: String, open var forceReload: Boolean, open var removeItemFlow: MutableSharedFlow<String>?)
data class StoriesRowItem(
  override val id: String,
  override var forceReload: Boolean,
  override var removeItemFlow: MutableSharedFlow<String>?,
  val moreButtonTitle: String,
  val cellType: StorytellerListViewCellType,
  val count: Int,
  val categories: List<String>,
  val title: String,
  val heightDp: Int,
) : Item(id, forceReload, removeItemFlow)

data class StoriesGridItem(
  override val id: String,
  override var forceReload: Boolean,
  override var removeItemFlow: MutableSharedFlow<String>?,
  val moreButtonTitle: String,
  val cellType: StorytellerListViewCellType,
  val count: Int,
  val categories: List<String>,
  val title: String,
) : Item(id, forceReload, removeItemFlow)

data class ClipsRowItem(
  override val id: String,
  override var forceReload: Boolean,
  override var removeItemFlow: MutableSharedFlow<String>?,
  val moreButtonTitle: String,
  val cellType: StorytellerListViewCellType,
  val count: Int,
  val title: String,
  val collection: String,
  val size: Size?,
  val heightDp: Int
) : Item(id, forceReload, removeItemFlow)

data class ClipsGridItem(
  override val id: String,
  override var forceReload: Boolean,
  override var removeItemFlow: MutableSharedFlow<String>?,

  val moreButtonTitle: String,
  val cellType: StorytellerListViewCellType,
  val count: Int,
  val title: String,
  val collection: String,
) : Item(id, forceReload,removeItemFlow)

data class StoriesSingletonItem(
  override val id: String,
  override var forceReload: Boolean,
  override var removeItemFlow: MutableSharedFlow<String>?,
  val categories: List<String>,
  val moreButtonTitle: String,
  val title: String,
) : Item(id, forceReload, removeItemFlow)

data class ClipsSingletonItem(
  override val id: String,
  override var forceReload: Boolean,
  val moreButtonTitle: String,
  val title: String,
  val collection: String,
  override var removeItemFlow: MutableSharedFlow<String>?,
) : Item(id, forceReload,removeItemFlow)

val VerticalVideoListItem.toEntity: Item?
  get() {
    val cellType = if (tileType == "round") {
      StorytellerListViewCellType.ROUND
    } else {
      StorytellerListViewCellType.SQUARE
    }
    val count = count ?: Int.MAX_VALUE
    val title = title.orEmpty()
    val moreButtonTitle = moreButtonTitle.orEmpty()
    val categories = categories.orEmpty()
    val collection = collection.orEmpty()
    val heightDp = when(size){
      Size.REGULAR -> 140
      Size.MEDIUM -> 200
      Size.LARGE -> 220
      null -> 0
    }

    return when {
      videoType == VideoType.STORIES && layout == Layout.ROW -> {
        StoriesRowItem(
          id = id,
          moreButtonTitle = moreButtonTitle,
          cellType = cellType,
          count = count,
          categories = categories,
          title = title,
          heightDp = heightDp,
          forceReload = true,
          removeItemFlow = null
        )
      }

      videoType == VideoType.STORIES && layout == Layout.GRID -> {
        StoriesGridItem(
          id = id,
          moreButtonTitle = moreButtonTitle,
          cellType = cellType,
          count = count,
          categories = categories,
          title = title,
          forceReload = true,
          removeItemFlow = null
        )
      }

      videoType == VideoType.CLIPS && layout == Layout.ROW -> {
        ClipsRowItem(
          id = id,
          moreButtonTitle = moreButtonTitle,
          cellType = cellType,
          count = count,
          collection = collection,
          title = title,
          size = size,
          heightDp = heightDp,
          forceReload = true,
          removeItemFlow = null
        )
      }

      videoType == VideoType.CLIPS && layout == Layout.GRID -> {
        ClipsGridItem(
          id = id,
          moreButtonTitle = moreButtonTitle,
          cellType = cellType,
          count = count,
          collection = collection,
          title = title,
          forceReload = true,
          removeItemFlow = null)
      }
      videoType == VideoType.STORIES && layout == Layout.SINGLETON -> {
        StoriesSingletonItem(
          id = id,
          moreButtonTitle = moreButtonTitle,
          categories = categories,
          title = title,
          forceReload = true,
          removeItemFlow = null
        )
      }
      videoType == VideoType.CLIPS && layout == Layout.SINGLETON -> {
        ClipsSingletonItem(
          id = id,
          moreButtonTitle = moreButtonTitle,
          collection = collection,
          title = title,
          forceReload = true,
          removeItemFlow = null
        )
      }
      else -> null
    }
  }


