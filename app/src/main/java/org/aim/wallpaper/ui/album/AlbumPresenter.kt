package org.aim.wallpaper.ui.album

import com.arellomobile.mvp.InjectViewState
import org.aim.wallpaper.domain.album.AlbumInteractor
import org.aim.wallpaper.ui.common.AppPresenter
import javax.inject.Inject

@InjectViewState
class AlbumPresenter(val albumId: Long) : AppPresenter<AlbumView>() {

    @Inject
    lateinit var interactor: AlbumInteractor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        onLoad()
    }

    fun onLoad() {
        interactor.albumId = albumId
        interactor.getPhotos().subscribe { list, throwable ->
            list?.apply { viewState.showList(this) }
            throwable?.apply { viewState.showError(this) }
        }
    }
}
