package org.aim.wallpaper.data.glide

import android.graphics.Bitmap
import io.reactivex.Single
import org.aim.wallpaper.data.ImageSize

interface ImageRepository {

    fun getImage(source: String, imageSize: ImageSize): Single<Bitmap>
}