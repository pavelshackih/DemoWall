package org.aim.wallpaper.ui.photo

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import org.aim.wallpaper.domain.Photo

class SectionsPagerAdapter(var list: List<Photo>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var currentFragment: Fragment? = null

    override fun getItem(position: Int): Fragment = PhotoFragment.newInstance(list[position].bigSource)

    override fun getCount(): Int = list.size

    override fun setPrimaryItem(container: ViewGroup?, position: Int, o: Any?) {
        if (currentFragment !== o) {
            currentFragment = o as Fragment
        }
        super.setPrimaryItem(container, position, o)
    }
}