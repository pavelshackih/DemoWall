package org.aim.wallpaper.ui.photo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v13.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.aim.wallpaper.App
import org.aim.wallpaper.R
import org.aim.wallpaper.domain.Photo
import org.aim.wallpaper.ext.setSafeOnClickListener
import org.aim.wallpaper.ui.AdapterPageListener
import org.aim.wallpaper.ui.common.AppActivity
import org.aim.wallpaper.ui.common.ErrorFragment

class PhotosActivity : AppActivity(), PhotoHostActivity, PhotoView {

    @InjectPresenter
    lateinit var presenter: PhotoPresenter

    @ProvidePresenter
    fun provideRepositoryPresenter(): PhotoPresenter {
        val presenter = PhotoPresenter(albumId, current)
        App.component.inject(presenter)
        return presenter
    }

    private val albumId: Long
        get() = intent.getLongExtra(ALBUM_ID, 0L)
    private val current: Int
        get() = intent.getIntExtra(CURRENT, 0)

    override fun showList(list: List<Photo>) {
        contentControls.visibility = View.VISIBLE
        adapter = SectionsPagerAdapter(list, fragmentManager)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(presenter.current, false)
    }

    override fun showProgressDialog() {
        val fragment = ProgressDialogFragment.newInstance()
        fragment.show(fragmentManager, ProgressDialogFragment.TAG)
    }

    override fun hideProgressDialog() {
        ProgressDialogFragment.hide(fragmentManager)
    }

    override fun onRetry() {
        ErrorFragment.hide(fragmentManager)
        presenter.onLoadPhotos()
    }

    override fun showError(error: Throwable) {
        contentControls.visibility = View.GONE
        val fragment = ErrorFragment.newInstance(error)
        fragment.show(fragmentManager)
    }

    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var contentControls: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.addOnPageChangeListener(AdapterPageListener { page -> presenter.onPageChanged(page) })

        val button = findViewById(R.id.set_button) as Button
        button.setSafeOnClickListener(View.OnClickListener {
            val permissionResult = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SET_WALLPAPER)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    val dialog = PermissionInfoDialog()
                    dialog.show(fragmentManager, "permission-info")
                } else {
                    onRequestPermission()
                }
            } else {
                applyWallpaper()
            }
        })
        contentControls = findViewById(R.id.content_controls) as LinearLayout

        val backButton = findViewById(R.id.back_button) as ImageButton
        backButton.setSafeOnClickListener(View.OnClickListener { ActivityCompat.finishAfterTransition(this) })

        val shareButton = findViewById(R.id.share_button) as ImageButton
        shareButton.setSafeOnClickListener(View.OnClickListener { onShareClick() })
    }

    private fun onShareClick() {
        if (viewPager.adapter == null) {
            Toast.makeText(this, R.string.image_loading, Toast.LENGTH_SHORT).show()
            return
        }
        val fragment = adapter.currentFragment as PhotoFragment
        presenter.onShare(fragment.source)
    }

    override fun onRequestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.SET_WALLPAPER),
                PhotosActivity.PERMISSIONS_REQUEST_SET_WALLPAPER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PhotosActivity.PERMISSIONS_REQUEST_SET_WALLPAPER) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                applyWallpaper()
            }
        }
    }

    private fun applyWallpaper() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val dialog = SetOnDialog()
            dialog.show(fragmentManager, "set-on-dialog")
        } else {
            onWallpaperTypeSelected(0)
        }
    }

    override fun onWallpaperTypeSelected(type: Int) {
        if (viewPager.adapter == null) {
            Toast.makeText(this, R.string.image_loading, Toast.LENGTH_SHORT).show()
            return
        }
        val photoFragment = adapter.currentFragment as PhotoFragment
        presenter.onApplyWallpaper(photoFragment.source, type)
    }

    companion object {

        private const val PERMISSIONS_REQUEST_SET_WALLPAPER = 1
        private const val CURRENT = "CURRENT"
        private const val ALBUM_ID = "ALBUM_ID"

        fun newIntent(context: Context, current: Int, albumId: Long): Intent {
            val intent = Intent(context, PhotosActivity::class.java)
            intent.putExtra(CURRENT, current)
            intent.putExtra(ALBUM_ID, albumId)
            return intent
        }
    }
}
