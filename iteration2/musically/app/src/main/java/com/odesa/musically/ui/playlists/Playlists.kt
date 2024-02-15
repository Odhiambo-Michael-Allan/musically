package com.odesa.musically.ui.playlists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.odesa.musically.ui.components.TopAppBar

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel,
    onSettingsClicked: () -> Unit
) {
    PlaylistsScreenContent(
        onSettingsClicked
    )
}

@Composable
fun PlaylistsScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "Playlists",
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