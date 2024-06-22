package com.odesa.musicMatters.core.model

import android.net.Uri

data class Album(
    val title: String,
    val artists: Set<String>,
    val artworkUri: Uri?
)
