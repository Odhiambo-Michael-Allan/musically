package com.odesa.musicMatters.ui.genres

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.ui.components.TopAppBar

@Composable
fun GenresScreen(
    viewModel: GenresViewModel,
    onSettingsClicked: () -> Unit
){
    GenresScreenContent(
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun GenresScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "Genres",
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
            Text(
                modifier = Modifier.align( Alignment.Center ),
                text = "Coming Soon!!"
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun GenresScreenContentPreview() {
    GenresScreenContent {}
}