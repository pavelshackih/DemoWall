package org.aim.wallpaper.data.config

import io.reactivex.Single

interface AppConfigRepository {

    val appConfig: Single<AppConfig>
}