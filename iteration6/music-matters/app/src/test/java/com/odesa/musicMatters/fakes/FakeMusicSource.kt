package com.odesa.musicMatters.fakes

import androidx.media3.common.MediaItem
import com.odesa.musicMatters.services.media.library.AbstractMusicSource
import com.odesa.musicMatters.services.media.library.STATE_ERROR
import com.odesa.musicMatters.services.media.library.STATE_INITIALIZED

class FakeMusicSource (
    private val music: List<MediaItem>
) : AbstractMusicSource(), Iterable<MediaItem> by music {

    override suspend fun load() = Unit

    fun prepare() {
        state = STATE_INITIALIZED
    }

    fun error() {
        state = STATE_ERROR
    }
}