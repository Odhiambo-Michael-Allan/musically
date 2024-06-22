package com.odesa.musicMatters.core.model

import android.net.Uri
import androidx.media3.common.MediaItem

data class Song(
    val id: String,
    val mediaUri: Uri,
    val title: String,
    val displayTitle: String,
    val trackNumber: Int?,
    val year: Int?,
    val duration: Long,
    val albumTitle: String?,
    val genre: String?,
    val artists: Set<String>,
    val composer: String?,
    val dateModified: Long,
    val size: Long,
    val path: String,
    val artworkUri: Uri?,
    val mediaItem: MediaItem
)
