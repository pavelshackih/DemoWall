package org.aim.wallpaper.ui.photo

import com.arellomobile.mvp.InjectViewState
import org.aim.wallpaper.domain.album.AlbumInteractor
import org.aim.wallpaper.domain.photo.PhotoInteractor
import org.aim.wallpaper.ui.common.AppPresenter
import javax.inject.Inject

@InjectViewState
class PhotoPresenter(val albumId: Long, var current: Int) : AppPresenter<PhotoView>() {

    @Inject
    lateinit var albumInteractor: AlbumInteractor

    @Inject
    lateinit var photoInteractor: PhotoInteractor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        onLoadPhotos()
    }

    fun onLoadPhotos() {
        albumInteractor.albumId = albumId
        albumInteractor.getPhotos().subscribe { list, throwable ->
            list?.apply { viewState.showList(this) }
            throwable?.apply { viewState.showError(this) }
        }
    }

    fun onPageChanged(page: Int) {
        current = page
    }

    fun onShare(source: String) {
        viewState.showProgressDialog()
        photoInteractor.shareImage(source).subscribe(
                { viewState.hideProgressDialog() },
                {
                    viewState.hideProgressDialog()
                    viewState.showError(it)
                })
    }

    fun onApplyWallpaper(source: String, type: Int) {
        viewState.showProgressDialog()
        photoInteractor.applyWallpaper(source, type).subscribe(
                { viewState.hideProgressDialog() },
                {
                    viewState.hideProgressDialog()
                    viewState.showError(it)
                })
    }
}