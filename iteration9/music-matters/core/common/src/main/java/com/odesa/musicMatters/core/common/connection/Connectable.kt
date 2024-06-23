package com.odesa.musicMatters.core.common.connection

import androidx.media3.common.MediaItem
import androidx.media3.common.Player

interface Connectable {
    val player: Player?
    suspend fun establishConnection()
    suspend fun getChildren( parentId: String ): List<MediaItem>
    fun addDisconnectListener( disconnectListener: () -> Unit )
}