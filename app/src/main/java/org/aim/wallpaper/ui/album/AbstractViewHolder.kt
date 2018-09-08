package org.aim.wallpaper.ui.album

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(params: T)
}