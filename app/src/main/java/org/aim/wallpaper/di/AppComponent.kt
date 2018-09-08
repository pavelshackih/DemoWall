package org.aim.wallpaper.di

import dagger.Component
import org.aim.wallpaper.domain.DomainModule
import org.aim.wallpaper.lang.LangModule
import org.aim.wallpaper.ui.album.AlbumPresenter
import org.aim.wallpaper.ui.main.MainPresenter
import org.aim.wallpaper.ui.photo.PhotoPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DomainModule::class, LangModule::class))
interface AppComponent {

    fun inject(presenter: MainPresenter)

    fun inject(presenter: AlbumPresenter)

    fun inject(presenter: PhotoPresenter)
}