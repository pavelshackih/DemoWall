package org.aim.wallpaper.domain.main

import io.reactivex.Single
import org.aim.wallpaper.data.VkPhotoRepository
import org.aim.wallpaper.domain.AbstractInteractor
import org.aim.wallpaper.domain.Album
import org.aim.wallpaper.executors.RxExecutors
import org.aim.wallpaper.ext.executeOn

class MainInteractor(private val repository: VkPhotoRepository,
                     rxExecutors: RxExecutors) : AbstractInteractor(rxExecutors) {

    fun getAlbums(): Single<List<Album>> {
        return repository.getAlbums().map { list ->
            list.map { item ->
                var title = item.title
                        .trim()
                        .replace(TITLE_REPLACE1, "")
                        .replace(TITLE_REPLACE2, "")
                        .replace("  ", " ")
                if (!title.contains("(") && title.contains("2")) {
                    title = title.replace("2", "(2)")
                }
                Album(item.id, title, "", item.findBySize("q"), item.findBySize("w"))
            }.sortedBy { it.title }
        }.executeOn(rxExecutors)
    }

    companion object {

        private const val TITLE_REPLACE1 = "(Обои 1080x1920)"
        private const val TITLE_REPLACE2 = "1080x1920"
    }
}