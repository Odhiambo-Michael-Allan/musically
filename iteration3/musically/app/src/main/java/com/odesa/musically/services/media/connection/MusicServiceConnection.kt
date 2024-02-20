package com.odesa.musically.services.media.connection

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface MusicServiceConnection {
    val nowPlaying: StateFlow<MediaItem>
    val playbackState: StateFlow<PlaybackState>
    val player: Player?
    suspend fun getChildren( parentId: String ): ImmutableList<MediaItem>
    suspend fun sendCommand( command: String, parameters: Bundle? ) : Boolean
    suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ( ( Int, Bundle? ) -> Unit ) ) : Boolean
}