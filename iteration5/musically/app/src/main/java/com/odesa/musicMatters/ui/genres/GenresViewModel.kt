package com.odesa.musicMatters.ui.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GenresViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class GenresViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( GenresViewModel() as T )
}