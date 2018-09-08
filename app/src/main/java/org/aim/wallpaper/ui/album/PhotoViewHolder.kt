package org.aim.wallpaper.ui.album

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import org.aim.wallpaper.R
import org.aim.wallpaper.ext.setSafeOnClickListener
import org.aim.wallpaper.ui.ActionViewItem
import com.bumptech.glide.request.target.Target as GlideTarget

class PhotoViewHolder(itemView: View, val action: ActionViewItem, val requestManager: RequestManager)
    : AbstractViewHolder<String>(itemView) {

    private val imageView = itemView.findViewById(R.id.image_view) as ImageView

    init {
        imageView.setSafeOnClickListener(View.OnClickListener { action(itemView, adapterPosition) })
    }

    override fun bind(params: String) {
        requestManager.load(Uri.parse(params))
                .crossFade()
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(imageView)
    }
}