package com.odesa.musically.fakes

import android.support.v4.media.MediaBrowserCompat
import com.odesa.musically.services.media.browser.MusicallyMediaBrowser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeMusicallyMediaBrowser : MusicallyMediaBrowser {

    override val root = "/"

    private val _isConnected = MutableStateFlow( false )
    override val isConnected = _isConnected.asStateFlow()

    override fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        callback.onChildrenLoaded( parentId, emptyList() )
    }

    override fun unsubscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        TODO("Not yet implemented")
    }

    fun setConnected( isConnected: Boolean ) {
        _isConnected.value = isConnected
    }
}