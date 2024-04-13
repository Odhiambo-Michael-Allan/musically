package com.odesa.musicMatters.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArtistsViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class ArtistsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( ArtistsViewModel() as T )
}