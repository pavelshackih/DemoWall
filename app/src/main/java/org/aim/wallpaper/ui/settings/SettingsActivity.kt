package org.aim.wallpaper.ui.settings

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v13.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import org.aim.wallpaper.BuildConfig
import org.aim.wallpaper.R
import java.util.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setupActionBar(toolbar)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, GeneralPreferenceFragment())
                    .commit()
        }
    }

    private fun setupActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class GeneralPreferenceFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)

            val locale = Locale.getDefault()
            val contactMePreference = findPreference(getString(R.string.pref_contact_me))
            if (locale?.language?.contains(RU, ignoreCase = true) ?: false) {
                preferenceScreen.removePreference(contactMePreference)
            } else {
                contactMePreference.setOnPreferenceClickListener {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
                    intent.type = "text/plain"
                    if (intent.resolveActivity(activity.packageManager) != null) {
                        activity.startActivity(intent)
                    }
                    true
                }
            }

            val mainPreference = findPreference(getString(R.string.pref_main_key))
            mainPreference.title = getString(R.string.app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE.toString())

            val libsPreference = findPreference(getString(R.string.pref_libs_key))
            libsPreference.setOnPreferenceClickListener {
                val notices = Notices()

                val list = ArrayList<Notice>()
                list.add(Notice("Kotlin 1.1", "https://kotlinlang.org/", "Copyright 2000–2017 JetBrains", ApacheSoftwareLicense20()))
                list.add(Notice("OkHttp", "https://github.com/square/okhttp", "Copyright 2016 Square, Inc.", ApacheSoftwareLicense20()))
                list.add(Notice("Moxy", "https://github.com/Arello-Mobile/Moxy", "Copyright (c) 2016 Arello Mobile", MITLicense()))
                list.add(Notice("Retrofit 2", "http://square.github.io/retrofit/", "Copyright 2013 Square, Inc.", ApacheSoftwareLicense20()))
                list.add(Notice("RxJava 2", "https://github.com/ReactiveX/RxJava", "Copyright (c) 2016-present, RxJava Contributors.", ApacheSoftwareLicense20()))
                list.add(Notice("Gson", "https://github.com/google/gson", "Copyright 2008 Google Inc.", ApacheSoftwareLicense20()))
                // list.add(Notice("CircleImageView", "https://github.com/hdodenhof/CircleImageView", "Copyright 2014 - 2017 Henning Dodenhof", ApacheSoftwareLicense20()))
                list.add(Notice("Plaid", "https://github.com/nickbutcher/plaid", "Copyright 2015 Google, Inc.", ApacheSoftwareLicense20()))
                list.add(Notice("Glide", "https://github.com/bumptech/glide", "", ApacheSoftwareLicense20()))
                list.add(Notice("Glide", "https://github.com/bumptech/glide", "", ApacheSoftwareLicense20()))
                list.add(Notice("Android In-App Billing v3 Library", "https://github.com/anjlab/android-inapp-billing-v3", "Copyright 2014 AnjLab", ApacheSoftwareLicense20()))
                list.sortBy { it.name }
                list.forEach { notices.addNotice(it) }

                LicensesDialog.Builder(activity)
                        .setNotices(notices)
                        .setTitle(R.string.open_soft_license)
                        .setCloseText(R.string.close)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show()
                true
            }
        }

        companion object {

            const val RU = "ru"
        }
    }
}
