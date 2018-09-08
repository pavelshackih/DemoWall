package org.aim.wallpaper.ui.common

import org.aim.wallpaper.lang.LangManager

interface HostActivity {
    fun showError(error: Throwable)
    fun onRetry()
    var langManager: LangManager
}