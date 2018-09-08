package org.aim.wallpaper.data.config

data class AppConfig(val serviceKey: String,
                     val ownerId: Long,
                     val albums: String,
                     val filtered: List<Long>)