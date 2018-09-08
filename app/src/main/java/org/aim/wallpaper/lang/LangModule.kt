package org.aim.wallpaper.lang

import android.app.Activity
import dagger.Module
import dagger.Provides
import org.aim.wallpaper.di.ActivityScope

@Module
class LangModule(private val activity: Activity) {

    @ActivityScope
    @Provides
    fun provideLangManager(): LangManager = LangManagerImpl(activity)
}