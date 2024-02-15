package com.odesa.musically

import android.app.Application
import com.odesa.musically.data.AppContainer
import com.odesa.musically.data.AppContainerImpl

class MusicallyApplication : Application() {

    // AppContainer instance used by the rest of the classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl( applicationContext )
    }

}
