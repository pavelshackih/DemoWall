package org.aim.wallpaper.ui.common

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import org.aim.wallpaper.lang.DaggerLangComponent
import org.aim.wallpaper.lang.LangManager
import org.aim.wallpaper.lang.LangModule
import javax.inject.Inject

abstract class AppActivity : MvpAppCompatActivity(), HostActivity {

    @Inject
    override lateinit var langManager: LangManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = DaggerLangComponent.builder()
                .langModule(LangModule(this))
                .build()
        component.inject(this)
    }
}