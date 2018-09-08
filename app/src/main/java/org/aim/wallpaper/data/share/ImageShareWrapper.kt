package org.aim.wallpaper.data.share

import android.graphics.Bitmap
import io.reactivex.Completable

interface ImageShareWrapper {

    fun shareImage(bitmap: Bitmap): Completable
}