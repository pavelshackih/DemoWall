package org.aim.wallpaper.data.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import io.reactivex.Completable

class WallpaperWrapperImpl(private val context: Context) : WallpaperWrapper {

    override fun applyImage(bitmap: Bitmap, type: Int): Completable =
            Completable.fromAction { internalApplyImage(bitmap, type) }

    private fun internalApplyImage(bitmap: Bitmap, type: Int) {
        val manager = WallpaperManager.getInstance(context)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val flag = when (type) {
                1 -> WallpaperManager.FLAG_LOCK
                2 -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                else -> WallpaperManager.FLAG_SYSTEM
            }
            manager.setBitmap(bitmap, null, true, flag)
        } else {
            manager.setBitmap(bitmap)
        }
    }
}