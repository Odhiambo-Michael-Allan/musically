package com.odesa.musicMatters.ui.albumArtists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlbumArtistsViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class AlbumArtistsViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ( AlbumArtistsViewModel() as T )
}