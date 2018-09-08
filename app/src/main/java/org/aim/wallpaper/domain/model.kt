package org.aim.wallpaper.domain

data class Album(val id: Long,
                 val title: String,
                 val desc: String,
                 val src: String,
                 val srcPreview: String)

data class Photo(val id: Long, val previewSource: String, val bigSource: String)