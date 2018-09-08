package org.aim.wallpaper.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.aim.wallpaper.executors.AppRxExecutors
import org.aim.wallpaper.executors.RxExecutors
import javax.inject.Singleton

@Singleton
@Module
class AppModule(val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideExecutors(): RxExecutors = AppRxExecutors()
}