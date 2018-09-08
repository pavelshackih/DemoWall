package org.aim.wallpaper.ui.photo

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.aim.wallpaper.R
import org.aim.wallpaper.ext.bundle
import java.lang.Exception

class PhotoFragment : Fragment() {

    lateinit var source: String

    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        source = arguments.getString(SOURCE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_photo, container, false)
        imageView = root.findViewById(R.id.image_view) as ImageView
        progressBar = root.findViewById(R.id.progress_bar) as ProgressBar

        Glide.with(imageView.context.applicationContext)
                .load(Uri.parse(source))
                .listener(object : RequestListener<Uri?, GlideDrawable?> {
                    override fun onException(e: Exception?,
                                             model: Uri?,
                                             target: Target<GlideDrawable?>?,
                                             isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?,
                                                 model: Uri?,
                                                 target: Target<GlideDrawable?>?,
                                                 isFromMemoryCache: Boolean,
                                                 isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .crossFade()
                .fitCenter()
                .into(imageView)

        ViewCompat.animate(progressBar)
                .alphaBy(1F)
                .setStartDelay(800L)
                .start()

        return root
    }

    companion object {

        private const val SOURCE = "SOURCE"

        fun newInstance(source: String): PhotoFragment {
            val fragment = PhotoFragment()
            fragment.arguments = bundle { putString(SOURCE, source) }
            return fragment
        }
    }
}