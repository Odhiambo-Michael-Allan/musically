package com.odesa.musicMatters.ui.forYou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.odesa.musicMatters.data.settings.SettingsRepository

class ForYouViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class ForYouViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( ForYouViewModel() as T )
}