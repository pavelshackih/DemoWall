package org.aim.wallpaper.domain.photo

import io.reactivex.Completable
import io.reactivex.Single
import org.aim.wallpaper.data.ImageSize
import org.aim.wallpaper.data.glide.ImageRepository
import org.aim.wallpaper.data.share.ImageShareWrapper
import org.aim.wallpaper.data.wallpaper.WallpaperWrapper
import org.aim.wallpaper.domain.AbstractInteractor
import org.aim.wallpaper.domain.Photo
import org.aim.wallpaper.domain.album.AlbumInteractor
import org.aim.wallpaper.executors.RxExecutors
import org.aim.wallpaper.ext.executeOn

class PhotoInteractor(
        private val albumInteractor: AlbumInteractor,
        private val wallpaperWrapper: WallpaperWrapper,
        private val shareWrapper: ImageShareWrapper,
        private val imageRepository: ImageRepository,
        rxExecutors: RxExecutors) : AbstractInteractor(rxExecutors) {

    fun shareImage(source: String): Completable =
            imageRepository.getImage(source, ImageSize.DefaultImageSize())
                    .flatMapCompletable { shareWrapper.shareImage(it) }
                    .executeOn(rxExecutors)

    fun getPhotos(): Single<List<Photo>> = albumInteractor.getPhotos()

    fun applyWallpaper(source: String, type: Int): Completable =
            imageRepository.getImage(source, ImageSize.DefaultImageSize())
                    .flatMapCompletable { wallpaperWrapper.applyImage(it, type) }
                    .executeOn(rxExecutors)
}