package org.aim.wallpaper.data.wallpaper

import android.graphics.Bitmap
import io.reactivex.Completable

interface WallpaperWrapper {

    fun applyImage(bitmap: Bitmap, type: Int): Completable
}