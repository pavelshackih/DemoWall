package org.aim.wallpaper.ui.photo

import org.aim.wallpaper.ui.common.HostActivity

interface PhotoHostActivity : HostActivity {

    fun onWallpaperTypeSelected(type: Int)

    fun onRequestPermission()
}