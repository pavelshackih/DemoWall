package org.aim.wallpaper.lang

import dagger.Component
import org.aim.wallpaper.di.ActivityScope
import org.aim.wallpaper.ui.common.AppActivity

@ActivityScope
@Component(modules = arrayOf(LangModule::class))
interface LangComponent {

    fun inject(activity: AppActivity)
}