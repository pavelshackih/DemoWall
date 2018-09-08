package org.aim.wallpaper.data.glide

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import io.reactivex.Single
import org.aim.wallpaper.data.ImageSize

class ImageRepositoryImpl(context: Context) : ImageRepository {

    private val requestManager = Glide.with(context)

    override fun getImage(source: String, imageSize: ImageSize): Single<Bitmap> {
        return Single.fromCallable {
            requestManager.load(Uri.parse(source))
                    .asBitmap()
                    .fitCenter()
                    .into(imageSize.size.first, imageSize.size.second)
                    .get()
        }
    }
}