package com.odesa.musically.fakes

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.google.common.collect.ImmutableList
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.PlaybackState
import kotlinx.coroutines.flow.StateFlow


class FakeMusicServiceConnection : MusicServiceConnection {

    override val nowPlaying: StateFlow<MediaItem>
        get() = TODO("Not yet implemented")
    override val playbackState: StateFlow<PlaybackState>
        get() = TODO("Not yet implemented")
    override val player: Player?
        get() = TODO("Not yet implemented")

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> {
        return ImmutableList.of()
    }

    override suspend fun sendCommand(command: String, parameters: Bundle?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: (Int, Bundle?) -> Unit
    ): Boolean {
        TODO("Not yet implemented")
    }

}