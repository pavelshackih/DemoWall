package org.aim.wallpaper.data.net

import com.google.gson.annotations.SerializedName

data class AlbumsResponse(val count: Int = 0, @SerializedName("response") val response: List<AlbumItem>)

data class AlbumItem(
        @SerializedName("aid") val id: Long = 0,
        @SerializedName("thumb_id") val thumbId: Long = 0,
        @SerializedName("owner_id") val ownerId: Long = 0,
        @SerializedName("title") val title: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("created") val created: Long = 0,
        @SerializedName("updated") val updated: Long = 0,
        @SerializedName("size") val size: Int = 0,
        @SerializedName("thumb_is_last") val thumbIsLast: Int = 0,
        @SerializedName("thumb_src") val thumbSrc: String = "",
        @SerializedName("sizes") val sizes: List<Size>
) {
    fun findBySize(size: String) = sizes.find { size == it.type }?.src ?: throw IllegalStateException("Can't find image with size $size for item ${this}")
}

data class Size(@SerializedName("src") val src: String,
                @SerializedName("width") val width: Int,
                @SerializedName("height") val height: Int,
                @SerializedName("type") val type: String)

data class PhotosResponse(@SerializedName("response") val response: List<PhotoItem>)

data class PhotoItem(
        @SerializedName("pid") val pid: Long = 0,
        @SerializedName("owner_id") val ownerId: Long = 0,
        @SerializedName("user_id") val userId: Long = 0,
        @SerializedName("src") val src: String = "",
        @SerializedName("src_big") val srcBig: String = "",
        @SerializedName("src_small") val srcSmall: String = "",
        @SerializedName("src_xbig") val srcXbig: String = "",
        @SerializedName("src_xxbig") val srcXxbig: String = "",
        @SerializedName("src_xxxbig") val srcXxxbig: String = "",
        @SerializedName("width") val width: Int = 0,
        @SerializedName("height") val height: Int = 0,
        @SerializedName("text") val text: String = "",
        @SerializedName("created") val created: Long = 0
)

data class PhotoResponse(@SerializedName("response") val response: List<Map<String, String>>)