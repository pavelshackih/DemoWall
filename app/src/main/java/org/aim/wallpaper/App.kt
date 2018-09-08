package org.aim.wallpaper

import android.app.Application
import org.aim.wallpaper.data.DataModule
import org.aim.wallpaper.di.AppComponent
import org.aim.wallpaper.di.AppModule
import org.aim.wallpaper.di.DaggerAppComponent
import org.aim.wallpaper.domain.DomainModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .dataModule(DataModule())
                .domainModule(DomainModule())
                .build()
    }

    companion object {

        lateinit var component: AppComponent
    }
}