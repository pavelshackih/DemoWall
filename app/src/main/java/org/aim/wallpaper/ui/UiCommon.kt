package org.aim.wallpaper.ui

import android.support.v4.view.ViewPager
import android.view.View

typealias ActionItem = (Int) -> Unit
typealias ActionViewItem = (View, Int) -> Unit

class AdapterPageListener(val f: ActionItem) : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        f(position)
    }
}

class DoubleClickPreventListener(private val listener: View.OnClickListener) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View?) {
        if (!preventDoubleClick()) {
            listener.onClick(v)
        }
    }

    fun preventDoubleClick(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime < DELTA) {
            return true
        }
        lastClickTime = currentTimeMillis
        return false
    }

    private companion object {

        private const val DELTA = 300
    }
}