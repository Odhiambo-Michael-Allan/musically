package com.odesa.musicMatters.ui.albumArtists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.odesa.musicMatters.ui.components.TopAppBar

@Composable
fun AlbumArtistsScreen(
    viewModel: AlbumArtistsViewModel,
    onSettingsClicked: () -> Unit
) {
    AlbumArtistsScreenContent(
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun AlbumArtistsScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "Album artists",
                rescan = "rescan",
                onRefreshClicked = { /*TODO*/ },
                settings = "Settings",
                onSettingsClicked = onSettingsClicked
            )
        }
    ){
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            Text( text = "Coming Soon!!" )
        }
    }
}