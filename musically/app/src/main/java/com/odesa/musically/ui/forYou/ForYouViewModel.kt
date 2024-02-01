package com.odesa.musically.ui.forYou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.odesa.musically.data.settings.SettingsRepository

class ForYouViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class ForYouViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( ForYouViewModel() as T )
}