package com.odesa.musically.ui.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TreeViewModel : ViewModel() {
}

@Suppress( "UNCHECKED_CAST" )
class TreeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( TreeViewModel() as T )
}