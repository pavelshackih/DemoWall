package org.aim.wallpaper.data

data class AppConfig(val serviceKey: String, val ownerId: Long, val albums: String, val filtered: List<Long>)