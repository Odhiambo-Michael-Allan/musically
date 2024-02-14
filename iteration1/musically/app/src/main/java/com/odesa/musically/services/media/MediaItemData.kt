package com.odesa.musically.services.media

import android.net.Uri

/**
 * Data class to encapsulate properties of a [MediaItem].
 *
 * If an item is [browsable] it means that it has a list of child media items that can be
 * retrieved by passing the mediaId to [MediaBrowserCompat.subscribe].
 */
data class MediaItemData (
    val mediaId: String,
    val title: String,
    val subtitle: String,
    val albumArtUri: Uri,
    val browsable: Boolean = false,
)

data class Song(
    val id: String,
    val mediaUri: Uri,
    val title: String,
    val trackNumber: Long?,
    val year: Long?,
    val duration: Long,
    val album: String?,
    val artists: String?,
    val composers: String?,
    val dateModified: Long,
    val size: Long,
    val path: String,
    val artworkUri: Uri?
)