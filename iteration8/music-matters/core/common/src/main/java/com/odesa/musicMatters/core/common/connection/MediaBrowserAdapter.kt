package com.odesa.musicMatters.core.common.connection

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.guava.await

class MediaBrowserAdapter(
    private val context: Context,
    private val serviceComponentName: ComponentName
) : Connectable {

    private var browser: MediaBrowser? = null
    private val disconnectListeners: MutableList<() -> Unit> = mutableListOf()

    override val player: Player?
        get() = browser

    override suspend fun establishConnection() {
        val newBrowser =
            MediaBrowser.Builder( context, SessionToken( context, serviceComponentName ) )
                .setListener( BrowserListener() )
                .buildAsync()
                .await()
        browser = newBrowser
        newBrowser.getLibraryRoot( /* params = */ null ).await().value
    }

    override suspend fun getChildren( parentId: String ): List<MediaItem> {
        val children = this.browser?.getChildren( parentId, 0, Int.MAX_VALUE, null )
            ?.await()?.value
        return children ?: ImmutableList.of()
    }

    override fun addDisconnectListener( disconnectListener: () -> Unit ) {
        disconnectListeners.add( disconnectListener )
    }

    private inner class BrowserListener : MediaBrowser.Listener {
        override fun onDisconnected( controller: MediaController ) {
            disconnectListeners.forEach {
                it.invoke()
            }
        }
    }
}