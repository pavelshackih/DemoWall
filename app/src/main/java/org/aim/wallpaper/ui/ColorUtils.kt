/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aim.wallpaper.ui

import android.graphics.Bitmap
import android.support.annotation.*
import android.support.annotation.IntRange
import android.support.v7.graphics.Palette

/**
 * Utility methods for working with colors.
 */
object ColorUtils {

    const val IS_LIGHT = 0
    const val IS_DARK = 1
    const val LIGHTNESS_UNKNOWN = 2

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    @CheckResult
    @ColorInt
    fun modifyAlpha(@ColorInt color: Int,
                    @IntRange(from = 0, to = 255) alpha: Int): Int {
        return color and 0x00ffffff or (alpha shl 24)
    }

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    @CheckResult
    @ColorInt
    fun modifyAlpha(@ColorInt color: Int,
                    @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        return modifyAlpha(color, (255f * alpha).toInt())
    }

    /**
     * Checks if the most populous color in the given palette is dark
     *
     *
     * Annoyingly we have to return this Lightness 'enum' rather than a boolean as palette isn't
     * guaranteed to find the most populous color.
     */
    @Lightness fun isDark(palette: Palette): Int {
        val mostPopulous = getMostPopulousSwatch(palette) ?: return LIGHTNESS_UNKNOWN
        return if (isDark(mostPopulous.hsl)) IS_DARK else IS_LIGHT
    }

    fun getMostPopulousSwatch(palette: Palette?): Palette.Swatch? {
        var mostPopulous: Palette.Swatch? = null
        if (palette != null) {
            for (swatch in palette.swatches) {
                if (mostPopulous == null || swatch.population > mostPopulous.population) {
                    mostPopulous = swatch
                }
            }
        }
        return mostPopulous
    }

    /**
     * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
     * with a large image!! If palette fails then check the color of the specified pixel
     */
    @JvmOverloads fun isDark(bitmap: Bitmap, backupPixelX: Int = bitmap.width / 2, backupPixelY: Int = bitmap.height / 2): Boolean {
        // first try palette with a small color quant size
        val palette = Palette.from(bitmap).maximumColorCount(3).generate()
        if (palette.swatches.size > 0) {
            return isDark(palette) == IS_DARK
        } else {
            // if palette failed, then check the color of the specified pixel
            return isDark(bitmap.getPixel(backupPixelX, backupPixelY))
        }
    }

    /**
     * Check that the lightness value (0–1)
     */
    fun isDark(hsl: FloatArray): Boolean { // @ImageSize(3)
        return hsl[2] < 0.5f
    }

    /**
     * Convert to HSL & check that the lightness value
     */
    fun isDark(@ColorInt color: Int): Boolean {
        val hsl = FloatArray(3)
        android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl)
        return isDark(hsl)
    }

    /**
     * Calculate a variant of the color to make it more suitable for overlaying information. Light
     * colors will be lightened and dark colors will be darkened

     * @param color the color to adjust
     * *
     * @param isDark whether `color` is light or dark
     * *
     * @param lightnessMultiplier the amount to modify the color e.g. 0.1f will alter it by 10%
     * *
     * @return the adjusted color
     */
    @ColorInt
    fun scrimify(@ColorInt color: Int,
                 isDark: Boolean,
                 @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float): Int {
        var lightnessMultiplierTmp = lightnessMultiplier
        val hsl = FloatArray(3)
        android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl)

        if (!isDark) {
            lightnessMultiplierTmp += 1f
        } else {
            lightnessMultiplierTmp = 1f - lightnessMultiplier
        }

        hsl[2] = constrain(0f, 1f, hsl[2] * lightnessMultiplierTmp)
        return android.support.v4.graphics.ColorUtils.HSLToColor(hsl)
    }

    fun constrain(min: Float, max: Float, v: Float): Float {
        return Math.max(min, Math.min(max, v))
    }

    @ColorInt
    fun scrimify(@ColorInt color: Int,
                 @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float): Int {
        return scrimify(color, isDark(color), lightnessMultiplier)
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(IS_LIGHT.toLong(), IS_DARK.toLong(), LIGHTNESS_UNKNOWN.toLong())
    annotation class Lightness
}
