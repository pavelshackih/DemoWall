package org.aim.wallpaper.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import jp.wasabeef.glide.transformations.CropCircleTransformation
import org.aim.wallpaper.App
import org.aim.wallpaper.R
import org.aim.wallpaper.domain.Album
import org.aim.wallpaper.ext.inflate
import org.aim.wallpaper.ext.setSafeOnClickListener
import org.aim.wallpaper.lang.LangManager
import org.aim.wallpaper.ui.ActionItem
import org.aim.wallpaper.ui.album.AlbumActivity
import org.aim.wallpaper.ui.common.AppFragment
import org.aim.wallpaper.ui.common.HostActivity

class MainFragment : AppFragment(), MainView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        val presenter = MainPresenter()
        App.component.inject(presenter)
        return presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_albums, container, false)
        recyclerView = root.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        progressBar = root.findViewById(R.id.progress_bar) as ProgressBar

        ViewCompat.animate(progressBar)
                .alphaBy(1F)
                .setStartDelay(400L)
                .start()

        return root
    }

    override fun showList(list: List<Album>) {
        progressBar.visibility = View.GONE
        val adapter = AlbumRecyclerAdapter(list, { position ->
            val item = list[position]
            val intent = AlbumActivity.newIntent(activity, item.id, item.title, item.srcPreview)
            activity.startActivity(intent)
        }, activity, langManager, Glide.with(activity))
        recyclerView.adapter = adapter
    }

    override fun showError(error: Throwable) {
        val host = activity as HostActivity
        host.showError(error)
    }

    class AlbumViewHolder(itemView: View, action: ActionItem,
                          val context: Context,
                          val langManager: LangManager,
                          val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView = itemView.findViewById(R.id.titleTextView) as TextView
        private val imageView = itemView.findViewById(R.id.imageView) as ImageView

        init {
            itemView.setSafeOnClickListener(View.OnClickListener { action.invoke(adapterPosition) })
        }

        fun bind(item: Album) {
            titleTextView.text = langManager.translate(item.title)
            requestManager.load(Uri.parse(item.src))
                    .crossFade()
                    .bitmapTransform(CropCircleTransformation(context))
                    .into(imageView)
        }
    }

    class AlbumRecyclerAdapter(val list: List<Album>,
                               val action: ActionItem,
                               val context: Context,
                               val langManager: LangManager,
                               val requestManager: RequestManager) : RecyclerView.Adapter<AlbumViewHolder>() {

        override fun getItemCount(): Int = list.count()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
                AlbumViewHolder(parent.inflate(R.layout.recycler_view_item), action, context, langManager, requestManager)

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            holder.bind(list[position])
        }
    }
}