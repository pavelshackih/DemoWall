package org.aim.wallpaper.data

import io.reactivex.Single

interface PreferenceRepository {

    val appConfig: Single<AppConfig>
}