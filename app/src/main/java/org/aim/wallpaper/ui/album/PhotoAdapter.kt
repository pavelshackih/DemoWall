package org.aim.wallpaper.ui.album

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import org.aim.wallpaper.R
import org.aim.wallpaper.ext.inflate
import org.aim.wallpaper.ui.ActionItem
import org.aim.wallpaper.ui.ActionViewItem

class PhotoAdapter(val sources: List<String>, val action: ActionViewItem, val requestManager: RequestManager)
    : RecyclerView.Adapter<AbstractViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> =
            PhotoViewHolder(parent.inflate(R.layout.view_holder_it), action, requestManager)

    override fun getItemCount(): Int = sources.count()

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) =
            when (holder) {
                is PhotoViewHolder -> holder.bind(sources[position])
                else -> throw IllegalStateException("Wrong view type")
            }
}