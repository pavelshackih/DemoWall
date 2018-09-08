package org.aim.wallpaper.data

import android.content.Context
import dagger.Module
import dagger.Provides
import org.aim.wallpaper.data.config.AppConfigRepository
import org.aim.wallpaper.data.config.AppConfigRepositoryImpl
import org.aim.wallpaper.data.glide.ImageRepository
import org.aim.wallpaper.data.glide.ImageRepositoryImpl
import org.aim.wallpaper.data.net.VkPhotosApi
import org.aim.wallpaper.data.share.ImageShareWrapper
import org.aim.wallpaper.data.share.ImageShareWrapperImpl
import org.aim.wallpaper.data.wallpaper.WallpaperWrapper
import org.aim.wallpaper.data.wallpaper.WallpaperWrapperImpl
import org.aim.wallpaper.di.AppModule
import javax.inject.Singleton

@Module(includes = arrayOf(AppModule::class))
class DataModule {

    @Singleton
    @Provides
    fun providerPreferenceRepository(): AppConfigRepository = AppConfigRepositoryImpl()

    @Singleton
    @Provides
    fun providePhotoRepository(appConfigRepository: AppConfigRepository): VkPhotoRepository =
            VkPhotoRepositoryImpl(VkPhotosApi.INSTANCE, appConfigRepository)

    @Singleton
    @Provides
    fun provideImageShareRepository(context: Context): ImageShareWrapper = ImageShareWrapperImpl(context)

    @Singleton
    @Provides
    fun provideWallpaperWrapper(context: Context): WallpaperWrapper = WallpaperWrapperImpl(context)

    @Singleton
    @Provides
    fun provideImageRepository(context: Context): ImageRepository = ImageRepositoryImpl(context)
}