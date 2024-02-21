package com.getstoryteller.storytellershowcaseapp.remote.entities

import com.getstoryteller.storytellershowcaseapp.ui.features.dashboard.adapter.UiElement
import com.storyteller.domain.entities.StorytellerListViewCellType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// These model classes are used for communication with the sample external API
// The communication with this API is implemented in the ApiService.kt class
// None of this code is required to use the Storyteller SDK.
// However, it may be useful as you plan an integration with your own CMS.

@kotlinx.serialization.Serializable
data class ResponseApiDto<T>(
  @SerialName("data") val data: T,
)

@kotlinx.serialization.Serializable
data class ResponseApiListDto<T>(
  @SerialName("data") val data: List<T>,
)

@kotlinx.serialization.Serializable
data class TenantSettingsApiDto(
  @SerialName("tenantName") val tenantName: String,
  @SerialName("androidApiKey") val androidApiKey: String,
  @SerialName("topLevelClipsCollection") val topLevelClipsCollection: String?,
  @SerialName("tabsEnabled") val tabsEnabled: Boolean,
)

@kotlinx.serialization.Serializable
data class KeyValueDto(
  @SerialName("value") val key: String,
  @SerialName("name") val value: String,
)

@kotlinx.serialization.Serializable
data class TabDto(
  @SerialName("name") val name: String,
  @SerialName("value") val value: String,
  @SerialName("sortOrder") val sortOrder: Int,
)

data class TenantSettingsDto(
  val topLevelClipsCollection: String?,
  val tabsEnabled: Boolean,
)

@kotlinx.serialization.Serializable
data class StorytellerItemApiDto(
  @SerialName("categories") val categories: List<String>,
  @SerialName("collection") val collection: String?,
  @SerialName("title") val title: String?,
  @SerialName("moreButtonTitle") val moreButtonTitle: String?,
  @SerialName("layout") val layout: LayoutType,
  @SerialName("size") val size: ItemSize,
  @SerialName("sortOrder") val sortOrder: Int,
  @SerialName("tileType") val tileType: TileType,
  @SerialName("videoType") val videoType: VideoType,
  @SerialName("id") val id: String,
  @SerialName("count") val displayLimit: Int?,
) {
  fun toUiElement(): UiElement {
    val cellType = when (tileType) {
      TileType.RECTANGULAR -> StorytellerListViewCellType.SQUARE
      TileType.ROUND -> StorytellerListViewCellType.ROUND
    }
    val height = if (this.tileType == TileType.ROUND) {
      113
    } else {
      when (this.size) {
        ItemSize.SMALL -> 106
        ItemSize.MEDIUM -> 330
        ItemSize.LARGE -> 440
        ItemSize.REGULAR -> 220
      }
    }
    return when (layout) {
      LayoutType.ROW -> {
        when (videoType) {
          VideoType.STORY -> {
            UiElement.StoryRow(
              title = title.orEmpty(),
              more = moreButtonTitle.orEmpty(),
              cellType = cellType,
              categories = categories,
              forceDataReload = true,
              height = height,
            )
          }

          VideoType.CLIP -> {
            UiElement.ClipRow(
              title = title.orEmpty(),
              more = moreButtonTitle.orEmpty(),
              cellType = cellType,
              height = height,
              collection = collection.orEmpty(),
              forceDataReload = true,
            )
          }
        }
      }

      LayoutType.GRID -> {
        when (videoType) {
          VideoType.STORY -> {
            UiElement.StoryGrid(
              title = title.orEmpty(),
              more = moreButtonTitle.orEmpty(),
              cellType = cellType,
              categories = categories,
              forceDataReload = true,
            )
          }

          VideoType.CLIP -> {
            UiElement.ClipGrid(
              title = title.orEmpty(),
              more = moreButtonTitle.orEmpty(),
              cellType = cellType,
              collection = collection.orEmpty(),
              forceDataReload = true,
            )
          }
        }
      }

      LayoutType.SINGLETON -> {
        when (videoType) {
          VideoType.STORY -> {
            UiElement.StorySingleton(
              title = title.orEmpty(),
              more = moreButtonTitle.orEmpty(),
              cellType = cellType,
              categories = categories,
              forceDataReload = true,
            )
          }

          VideoType.CLIP -> {
            UiElement.ClipSingleton(
              title = title.orEmpty(),
              more = moreButtonTitle.orEmpty(),
              cellType = cellType,
              collection = collection.orEmpty(),
              forceDataReload = true,
            )
          }
        }
      }
    }
  }
}

@kotlinx.serialization.Serializable(with = LayoutTypeSerializer::class)
enum class LayoutType(val serializedName: String) {
  ROW("row"),
  GRID("grid"),
  SINGLETON("singleton"),
  ;

  companion object {
    fun deserialize(
      serialisedName: String,
    ): LayoutType =
      entries
        .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
  }
}

object LayoutTypeSerializer : KSerializer<LayoutType> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("LayoutType", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: LayoutType,
  ) {
    encoder.encodeString(value.serializedName)
  }

  override fun deserialize(
    decoder: Decoder,
  ): LayoutType {
    val key = decoder.decodeString()
    return LayoutType.deserialize(key)
  }
}

@kotlinx.serialization.Serializable(with = TileTypeSerializer::class)
enum class TileType(val serializedName: String) {
  RECTANGULAR("rectangular"),
  ROUND("round"),
  ;

  companion object {
    fun deserialize(
      serialisedName: String,
    ): TileType =
      entries
        .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
  }
}

object TileTypeSerializer : KSerializer<TileType> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("TileType", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: TileType,
  ) {
    encoder.encodeString(value.serializedName)
  }

  override fun deserialize(
    decoder: Decoder,
  ): TileType {
    val key = decoder.decodeString()
    return TileType.deserialize(key)
  }
}

@kotlinx.serialization.Serializable(with = VideoTypeSerializer::class)
enum class VideoType(val serializedName: String) {
  STORY("stories"),
  CLIP("clips"),
  ;

  companion object {
    fun deserialize(
      serialisedName: String,
    ): VideoType =
      entries
        .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
  }
}

object VideoTypeSerializer : KSerializer<VideoType> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("VideoType", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: VideoType,
  ) {
    encoder.encodeString(value.serializedName)
  }

  override fun deserialize(
    decoder: Decoder,
  ): VideoType {
    val key = decoder.decodeString()
    return VideoType.deserialize(key)
  }
}

@kotlinx.serialization.Serializable(with = ItemSizeSerializer::class)
enum class ItemSize(val serializedName: String) {
  MEDIUM("medium"),
  REGULAR("regular"),
  LARGE("large"),
  SMALL("small"),
  ;

  companion object {
    fun deserialize(
      serialisedName: String,
    ): ItemSize =
      entries
        .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
  }
}

object ItemSizeSerializer : KSerializer<ItemSize> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("ItemSize", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: ItemSize,
  ) {
    encoder.encodeString(value.serializedName)
  }

  override fun deserialize(
    decoder: Decoder,
  ): ItemSize {
    val key = decoder.decodeString()
    return ItemSize.deserialize(key)
  }
}
