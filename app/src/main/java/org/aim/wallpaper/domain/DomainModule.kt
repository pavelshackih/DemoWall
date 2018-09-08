package org.aim.wallpaper.domain

import dagger.Module
import dagger.Provides
import org.aim.wallpaper.data.DataModule
import org.aim.wallpaper.data.VkPhotoRepository
import org.aim.wallpaper.data.glide.ImageRepository
import org.aim.wallpaper.data.share.ImageShareWrapper
import org.aim.wallpaper.data.wallpaper.WallpaperWrapper
import org.aim.wallpaper.di.AppModule
import org.aim.wallpaper.domain.album.AlbumInteractor
import org.aim.wallpaper.domain.main.MainInteractor
import org.aim.wallpaper.domain.photo.PhotoInteractor
import org.aim.wallpaper.executors.RxExecutors
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class, AppModule::class))
class DomainModule {

    @Singleton
    @Provides
    fun provideAlbumsInteractor(repository: VkPhotoRepository, rxExecutors: RxExecutors) = MainInteractor(repository, rxExecutors)

    @Singleton
    @Provides
    fun providePhotosInteractor(repository: VkPhotoRepository, rxExecutors: RxExecutors) = AlbumInteractor(repository, rxExecutors)

    @Singleton
    @Provides
    fun provideShareInteractor(albumInteractor: AlbumInteractor,
                               wallpaperWrapper: WallpaperWrapper,
                               shareWrapper: ImageShareWrapper,
                               imageRepository: ImageRepository,
                               rxExecutors: RxExecutors) =
            PhotoInteractor(albumInteractor, wallpaperWrapper, shareWrapper, imageRepository, rxExecutors)
}