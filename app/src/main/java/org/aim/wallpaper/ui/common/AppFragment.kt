package org.aim.wallpaper.ui.common

import com.arellomobile.mvp.MvpFragment
import org.aim.wallpaper.lang.LangManager

open class AppFragment : MvpFragment() {

    val langManager: LangManager by lazy {
        if (activity is HostActivity) {
            val host = activity as HostActivity
            host.langManager
        } else {
            throw IllegalStateException()
        }
    }
}