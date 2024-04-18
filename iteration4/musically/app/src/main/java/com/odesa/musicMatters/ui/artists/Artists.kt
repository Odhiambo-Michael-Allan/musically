package com.odesa.musicMatters.ui.artists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.odesa.musicMatters.ui.components.TopAppBar

@Composable
fun ArtistsScreen(
    viewModel: ArtistsViewModel,
    onSettingsClicked: () -> Unit
) {
    ArtistsScreenContent(
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun ArtistsScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "Artists",
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