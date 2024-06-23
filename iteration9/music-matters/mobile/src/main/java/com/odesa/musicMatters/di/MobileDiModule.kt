package com.odesa.musicMatters.di

import android.content.Context
import com.odesa.musicMatters.core.common.di.CommonDiModule
import com.odesa.musicMatters.core.data.di.DataDiModule

class MobileDiModule( context: Context ) {
    private val dataDiModule = DataDiModule.getInstance( context )
    private val commonDiModule = CommonDiModule(
        context = context,
        playlistRepository = dataDiModule.playlistRepository,
        settingsRepository = dataDiModule.settingsRepository
    )
    val settingsRepository = dataDiModule.settingsRepository
    val playlistRepository = dataDiModule.playlistRepository
    val searchHistoryRepository = dataDiModule.searchHistoryRepository
    val musicServiceConnection = commonDiModule.musicServiceConnection
}