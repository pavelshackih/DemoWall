package org.aim.wallpaper.ui.photo

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import org.aim.wallpaper.R
import org.aim.wallpaper.ui.common.BottomListBuilder

class SetOnDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = BottomListBuilder(activity)
        val arr = resources.getStringArray(R.array.set_types)
        arr.forEachIndexed { index, s -> builder.item(index, s, 0) }
        builder.listener = { _, i ->
            val host = activity as PhotoHostActivity
            host.onWallpaperTypeSelected(i)
        }
        return builder.create()
    }
}