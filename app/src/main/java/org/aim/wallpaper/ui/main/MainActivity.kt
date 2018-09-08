package org.aim.wallpaper.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import org.aim.wallpaper.R
import org.aim.wallpaper.ui.common.AppActivity
import org.aim.wallpaper.ui.common.ErrorFragment
import org.aim.wallpaper.ui.donate.DonateDialog
import org.aim.wallpaper.ui.donate.DonateHost
import org.aim.wallpaper.ui.donate.ThankYouDialog
import org.aim.wallpaper.ui.settings.SettingsActivity

class MainActivity : AppActivity(), DonateHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_about) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        } else if (id == R.id.action_donate) {
            val dialog = DonateDialog()
            dialog.show(fragmentManager, "donate-dialog")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 32459) {
            val fragment = fragmentManager.findFragmentByTag("donate-dialog")
            if (fragment is DonateDialog) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun showError(error: Throwable) {
        val fragment = ErrorFragment.newInstance(error)
        fragment.show(fragmentManager)
    }

    override fun onRetry() {
        initFragment()
    }

    override fun onPurchased() {
        val fragment = ThankYouDialog()
        fragment.show(fragmentManager, "thank-you-dialog")
    }
}
