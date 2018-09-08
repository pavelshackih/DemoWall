package org.aim.wallpaper.data

import io.reactivex.Single
import org.aim.wallpaper.data.net.AlbumItem
import org.aim.wallpaper.data.net.PhotoItem

interface VkPhotoRepository {

    fun getAlbums(): Single<List<AlbumItem>>
    fun getPhotos(albumId: Long): Single<List<PhotoItem>>
}