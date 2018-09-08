package org.aim.wallpaper.ui.main

import com.arellomobile.mvp.InjectViewState
import org.aim.wallpaper.domain.main.MainInteractor
import org.aim.wallpaper.ui.common.AppPresenter
import javax.inject.Inject

@InjectViewState
class MainPresenter : AppPresenter<MainView>() {

    @Inject
    lateinit var interactor: MainInteractor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.getAlbums().subscribe { list, throwable ->
            list?.apply { viewState.showList(this) }
            throwable?.apply { viewState.showError(this) }
        }
    }
}