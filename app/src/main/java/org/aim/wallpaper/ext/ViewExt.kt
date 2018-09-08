package org.aim.wallpaper.ext

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.aim.wallpaper.ui.DoubleClickPreventListener

fun ViewGroup.inflate(@LayoutRes resource: Int): View = LayoutInflater.from(this.context).inflate(resource, this, false)

inline fun bundle(f: Bundle.() -> Unit): Bundle = Bundle().apply { f(this) }

fun View.setSafeOnClickListener(listener: View.OnClickListener) {
    this.setOnClickListener(DoubleClickPreventListener(listener))
}