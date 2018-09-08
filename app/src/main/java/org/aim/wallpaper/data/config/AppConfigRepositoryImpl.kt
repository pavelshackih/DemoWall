package org.aim.wallpaper.data.config

import com.google.android.gms.tasks.Tasks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.Single
import org.aim.wallpaper.BuildConfig

class AppConfigRepositoryImpl : AppConfigRepository {

    private val firebaseConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        firebaseConfig.setConfigSettings(configSettings)
    }

    private fun fetch(): Single<Boolean> {
        return Single.fromCallable {
            val tasks = firebaseConfig.fetch(getCachePeriod())
            Tasks.await(tasks)
            val result = tasks.isSuccessful
            if (result) {
                firebaseConfig.activateFetched()
            }
            result
        }
    }

    private fun getCachePeriod() = if (BuildConfig.DEBUG) 1L else CACHE_PERIOD

    override val appConfig: Single<AppConfig>
        get() = fetch().map { success ->
            if (success) {
                val filteredValue = firebaseConfig.getString(FILTERED_PHOTOS)
                val filtered = if (filteredValue.isNullOrEmpty()) emptyList<Long>() else filteredValue.split(",").map(String::toLong)
                AppConfig(firebaseConfig.getString(SERVICE_KEY),
                        firebaseConfig.getString(OWNER_ID).toLong(),
                        firebaseConfig.getString(ALBUMS),
                        filtered)
            } else {
                throw IllegalStateException("Can't load firebase config!")
            }
        }

    private companion object {

        const val CACHE_PERIOD = 43200L * 2
        const val SERVICE_KEY = "service_key"
        const val OWNER_ID = "owner_id"
        const val ALBUMS = "albums"
        const val FILTERED_PHOTOS = "filteredPhotos"
    }
}