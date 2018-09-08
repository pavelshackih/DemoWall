package org.aim.wallpaper.domain.album

import io.reactivex.Single
import org.aim.wallpaper.data.VkPhotoRepository
import org.aim.wallpaper.domain.AbstractInteractor
import org.aim.wallpaper.domain.Photo
import org.aim.wallpaper.executors.RxExecutors
import org.aim.wallpaper.ext.executeOn

class AlbumInteractor(private val repository: VkPhotoRepository, rxExecutors: RxExecutors) :
        AbstractInteractor(rxExecutors) {

    var albumId: Long = 0

    fun getPhotos(): Single<List<Photo>> {
        return repository.getPhotos(albumId)
                .map { it.map { Photo(it.pid, it.srcBig, it.srcXxxbig) }.reversed() }
                .executeOn(rxExecutors)
    }
}