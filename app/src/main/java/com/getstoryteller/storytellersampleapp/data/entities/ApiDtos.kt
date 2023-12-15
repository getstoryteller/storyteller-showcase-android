package com.getstoryteller.storytellersampleapp.data.entities

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@kotlinx.serialization.Serializable
data class ResponseApiDto<T>(@SerialName("data") val data: T)

@kotlinx.serialization.Serializable
data class ResponseApiListDto<T>(@SerialName("data") val data: List<T>)

@kotlinx.serialization.Serializable
data class TenantSettingsApiDto(
    @SerialName("tenantName") val tenantName: String,
    @SerialName("androidApiKey") val androidApiKey: String,
    @SerialName("topLevelClipsCollection") val topLevelClipsCollection: String?,
    @SerialName("tabsEnabled") val tabsEnabled: Boolean
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
    val tabsEnabled: Boolean
)

@kotlinx.serialization.Serializable
data class StorytellerItemApiDto(
    @SerialName("categories") val categories: List<String>,
    @SerialName("collection") val collection: String?,
    @SerialName("title") val title: String?,
    @SerialName("layout") val layout: LayoutType,
    @SerialName("size") val size: ItemSize,
    @SerialName("sortOrder") val sortOrder: Int,
    @SerialName("tileType") val tileType: TileType,
    @SerialName("videoType") val videoType: VideoType,
    @SerialName("id") val id: String,
    @SerialName("count") val displayLimit: Int?,
)

@kotlinx.serialization.Serializable(with = LayoutTypeSerializer::class)
enum class LayoutType(val serializedName: String) {
    ROW("row"), GRID("grid"), SINGLETON("singleton");

    companion object {
        fun deserialize(serialisedName: String): LayoutType = values()
            .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
    }
}

@Serializer(forClass = LayoutType::class)
object LayoutTypeSerializer : KSerializer<LayoutType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LayoutType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LayoutType) {
        encoder.encodeString(value.serializedName)
    }

    override fun deserialize(decoder: Decoder): LayoutType {
        val key = decoder.decodeString()
        return LayoutType.deserialize(key)
    }
}

@kotlinx.serialization.Serializable(with = TileTypeSerializer::class)
enum class TileType(val serializedName: String) {
    RECTANGULAR("rectangular"), ROUND("round");

    companion object {
        fun deserialize(serialisedName: String): TileType = values()
            .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
    }
}

@Serializer(forClass = LayoutType::class)
object TileTypeSerializer : KSerializer<TileType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TileType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TileType) {
        encoder.encodeString(value.serializedName)
    }

    override fun deserialize(decoder: Decoder): TileType {
        val key = decoder.decodeString()
        return TileType.deserialize(key)
    }
}

@kotlinx.serialization.Serializable(with = VideoTypeSerializer::class)
enum class VideoType(val serializedName: String) {
    STORY("stories"), CLIP("clips");

    companion object {
        fun deserialize(serialisedName: String): VideoType = values()
            .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
    }
}

@Serializer(forClass = VideoType::class)
object VideoTypeSerializer : KSerializer<VideoType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("VideoType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: VideoType) {
        encoder.encodeString(value.serializedName)
    }

    override fun deserialize(decoder: Decoder): VideoType {
        val key = decoder.decodeString()
        return VideoType.deserialize(key)
    }
}

@kotlinx.serialization.Serializable(with = ItemSizeSerializer::class)
enum class ItemSize(val serializedName: String) {
    MEDIUM("medium"), REGULAR("regular"), LARGE("large"), SMALL("small");

    companion object {
        fun deserialize(serialisedName: String): ItemSize = values()
            .first { it.serializedName.equals(serialisedName, ignoreCase = true) }
    }
}

@Serializer(forClass = ItemSize::class)
object ItemSizeSerializer : KSerializer<ItemSize> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ItemSize", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ItemSize) {
        encoder.encodeString(value.serializedName)
    }

    override fun deserialize(decoder: Decoder): ItemSize {
        val key = decoder.decodeString()
        return ItemSize.deserialize(key)
    }
}