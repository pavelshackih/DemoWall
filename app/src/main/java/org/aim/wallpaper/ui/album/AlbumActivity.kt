package org.aim.wallpaper.ui.album

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v13.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation
import org.aim.wallpaper.App
import org.aim.wallpaper.R
import org.aim.wallpaper.domain.Photo
import org.aim.wallpaper.ui.ColorUtils
import org.aim.wallpaper.ui.common.AppActivity
import org.aim.wallpaper.ui.common.ErrorFragment
import org.aim.wallpaper.ui.common.HostActivity
import org.aim.wallpaper.ui.photo.PhotosActivity
import java.lang.Exception

class AlbumActivity : AppActivity(), AlbumView, HostActivity {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbarImageView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar

    @InjectPresenter
    lateinit var presenter: AlbumPresenter

    @ProvidePresenter
    fun provideRepositoryPresenter(): AlbumPresenter {
        val presenter = AlbumPresenter(albumId)
        App.component.inject(presenter)
        return presenter
    }

    private val albumId: Long
        get() = intent.getLongExtra(ALBUM_ID, 0L)

    private val preview: String
        get() = intent.getStringExtra(PREVIEW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        setContentView(R.layout.activity_album)

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toolbarLayout = findViewById(R.id.toolbar_layout) as CollapsingToolbarLayout
        toolbarLayout.title = langManager.translate(intent.getStringExtra(TITLE))

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.itemAnimator = DefaultItemAnimator()
        val gridLayoutManager = GridLayoutManager(this, resources.getInteger(R.integer.num_columns))
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        toolbarImageView = findViewById(R.id.toolbar_image_view) as ImageView

        progressBar = findViewById(R.id.progress_bar) as ProgressBar

        ViewCompat.animate(progressBar)
                .alphaBy(1F)
                .setStartDelay(400L)
                .start()

        Glide.with(this).load(preview)
                .listener(object : RequestListener<String?, GlideDrawable?> {
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable?>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        if (resource is GlideBitmapDrawable) {
                            val bitmap = resource.bitmap
                            Palette.from(bitmap)
                                    .clearFilters()
                                    .maximumColorCount(3)
                                    .setRegion(50, bitmap.height / 2, bitmap.width / 2, bitmap.height / 2 + 50)
                                    .generate {
                                        val swatch = it.dominantSwatch
                                        if (swatch != null) {
                                            val color = ColorUtils.scrimify(swatch.titleTextColor, 0.5F)
                                            toolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(color))
                                        }
                                    }
                        }
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable?>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .crossFade()
                .centerCrop()
                .bitmapTransform(BlurTransformation(applicationContext))
                .into(toolbarImageView)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showError(error: Throwable) {
        progressBar.visibility = View.GONE
        val fragment = ErrorFragment.newInstance(error)
        fragment.show(fragmentManager)
    }

    override fun showList(list: List<Photo>) {
        progressBar.visibility = View.GONE
        val adapterList = list.map { it.previewSource }
        val adapter = PhotoAdapter(adapterList, { view, position ->
            val options = ActivityOptionsCompat.makeScaleUpAnimation(view,
                    view.width / 2, view.height / 2, 0, 0)
            ActivityCompat.startActivity(this, PhotosActivity.newIntent(this, position, albumId), options.toBundle())
        }, Glide.with(this))
        recyclerView.adapter = adapter
    }

    override fun onRetry() {
        ErrorFragment.hide(fragmentManager)
        presenter.onLoad()
    }

    companion object {

        private const val ALBUM_ID = "ALBUM_ID"
        private const val TITLE = "TITLE"
        private const val PREVIEW = "PREVIEW"

        fun newIntent(context: Context, albumId: Long, title: String, preview: String): Intent {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra(ALBUM_ID, albumId)
            intent.putExtra(TITLE, title)
            intent.putExtra(PREVIEW, preview)
            return intent
        }
    }
}