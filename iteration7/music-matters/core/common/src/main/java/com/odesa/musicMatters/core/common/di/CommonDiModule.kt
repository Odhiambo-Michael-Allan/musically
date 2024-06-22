package com.odesa.musicMatters.core.common.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.PlaybackParameters
import com.odesa.musicMatters.core.common.connection.MediaBrowserAdapter
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.common.connection.TestableMusicServiceConnection
import com.odesa.musicMatters.core.common.media.MusicService
import com.odesa.musicMatters.core.data.preferences.toRepeatMode
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers


class CommonDiModule(
    context: Context,
    settingsRepository: SettingsRepository
) {

    private val mediaBrowserAdapter = MediaBrowserAdapter(
        context = context,
        serviceComponentName = ComponentName( context, MusicService::class.java )
    )
    val musicServiceConnection: MusicServiceConnection =
        TestableMusicServiceConnection.getInstance(
            connectable = mediaBrowserAdapter,
            dispatcher = Dispatchers.Main,
            playbackParameters = PlaybackParameters(
                settingsRepository.currentPlaybackSpeed.value,
                settingsRepository.currentPlaybackPitch.value
            ),
            repeatMode = settingsRepository.currentLoopMode.value.toRepeatMode(),
        )
}

