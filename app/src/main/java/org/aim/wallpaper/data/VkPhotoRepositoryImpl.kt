package org.aim.wallpaper.data

import android.support.v4.util.LongSparseArray
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.aim.wallpaper.data.config.AppConfig
import org.aim.wallpaper.data.config.AppConfigRepository
import org.aim.wallpaper.data.net.AlbumItem
import org.aim.wallpaper.data.net.PhotoItem
import org.aim.wallpaper.data.net.VkPhotosApi
import org.aim.wallpaper.ext.maybeFromNull

class VkPhotoRepositoryImpl(private val vkPhotosApi: VkPhotosApi,
                            private val preferences: AppConfigRepository) : VkPhotoRepository {

    override fun getAlbums(): Single<List<AlbumItem>> {
        return preferences.appConfig.map { (serviceKey, ownerId, albums) ->
            vkPhotosApi.getAlbums(ownerId, albums, ENABLED, ENABLED, serviceKey)
        }.flatMap { it }.map { it.response }
    }

    override fun getPhotos(albumId: Long): Single<List<PhotoItem>> {
        return Observable.concat(
                getPhotosFromCache(albumId).toObservable(),
                getPhotosFromNetwork(albumId).toObservable())
                .firstOrError()
    }

    private fun getPhotosFromNetwork(albumId: Long): Single<List<PhotoItem>> {
        return preferences.appConfig.map { (serviceKey, ownerId) ->
            vkPhotosApi.get(ownerId, albumId, serviceKey)
        }
                .flatMap { it }
                .map { it.response }
                .zipWith(preferences.appConfig,
                        BiFunction<List<PhotoItem>, AppConfig, List<PhotoItem>> { list, (_, _, _, filtered) ->
                            list.filter { !filtered.contains(it.pid) }
                        })
                .doOnSuccess { albumCache.put(albumId, it) }
    }

    private fun getPhotosFromCache(albumId: Long): Maybe<List<PhotoItem>> = maybeFromNull(albumCache[albumId])

    private companion object {

        const val ENABLED = 1
        val albumCache = LongSparseArray<List<PhotoItem>>()
    }
}