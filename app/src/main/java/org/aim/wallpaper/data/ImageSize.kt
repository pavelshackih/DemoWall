package org.aim.wallpaper.data

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

sealed class ImageSize {

    abstract val size: Pair<Int, Int>

    class DeviceScreen(private val context: Context) : ImageSize() {

        override val size: Pair<Int, Int>
            get() {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = wm.defaultDisplay
                val size = Point()
                display.getSize(size)
                return Pair(size.x, size.y)
            }
    }

    class DefaultImageSize : ImageSize() {

        override val size: Pair<Int, Int> = DEFAULT_IMAGE_SIZE

        companion object {

            private val DEFAULT_IMAGE_SIZE: Pair<Int, Int>
                get() = Pair(1080, 1920)
        }
    }

    class CustomSize(width: Int, height: Int) : ImageSize() {

        override val size: Pair<Int, Int> = Pair(width, height)
    }
}