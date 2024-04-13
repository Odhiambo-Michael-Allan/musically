package com.odesa.musicMatters.ui.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoldersViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class FoldersViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( FoldersViewModel() as T )
}