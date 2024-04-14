package com.odesa.musicMatters

import android.app.Application
import com.odesa.musicMatters.data.AppContainer
import com.odesa.musicMatters.data.AppContainerImpl

class MusicMatters : Application() {

    // AppContainer instance used by the rest of the classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl( applicationContext )
    }

}
