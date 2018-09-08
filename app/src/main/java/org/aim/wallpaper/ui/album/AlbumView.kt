package org.aim.wallpaper.ui.album

import org.aim.wallpaper.domain.Photo
import org.aim.wallpaper.ui.common.AppView

interface AlbumView : AppView {

    fun showList(list: List<Photo>)
}