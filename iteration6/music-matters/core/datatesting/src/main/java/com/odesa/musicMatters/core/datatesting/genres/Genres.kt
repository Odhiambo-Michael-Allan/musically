package com.odesa.musicMatters.core.datatesting.genres

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.odesa.musicMatters.core.model.Genre
import java.util.UUID

val testGenreMediaItems: List<MediaItem> = listOf(
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Hip Hop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Pop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Rock" )
            }.build()
        )
    }.build(),
)

val testGenres = testGenreMediaItems.map { Genre( it.mediaMetadata.title.toString(), 0 ) }