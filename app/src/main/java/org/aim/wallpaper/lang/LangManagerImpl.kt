package org.aim.wallpaper.lang

import android.content.Context
import org.aim.wallpaper.R
import java.util.*

class LangManagerImpl(context: Context) : LangManager {

    private val native = context.resources.getStringArray(R.array.albums_native)
    private val translated = context.resources.getStringArray(R.array.albums_translated)

    private fun isNeedTranslate(): Boolean = !DEFAULT_LOCALE.equals(Locale.getDefault().language, ignoreCase = true)

    override fun translate(input: String): String {
        if (!isNeedTranslate()) {
            return input
        }
        var mutableInput = input
        native.forEachIndexed { index, s ->
            if (input.contains(s)) {
                mutableInput = mutableInput.replace(s, translated[index])
            }
        }
        return mutableInput
    }

    private companion object {

        private const val DEFAULT_LOCALE = "ru"
    }
}