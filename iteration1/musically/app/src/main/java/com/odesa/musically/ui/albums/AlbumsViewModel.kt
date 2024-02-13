package com.odesa.musically.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlbumsViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class AlbumsViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( AlbumsViewModel() as T )
}