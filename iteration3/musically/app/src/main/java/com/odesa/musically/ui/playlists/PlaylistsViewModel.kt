package com.odesa.musically.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaylistsViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class PlaylistsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ( PlaylistsViewModel() as T )
}