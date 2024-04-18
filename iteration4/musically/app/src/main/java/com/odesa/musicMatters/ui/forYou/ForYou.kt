package com.odesa.musicMatters.ui.forYou

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
fun ForYouScreen(
    viewModel: ForYouViewModel,
    onSettingsClicked: () -> Unit
) {
    ForYouScreenContent(
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun ForYouScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "For You",
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
fun ForYouScreenContentPreview() {
    ForYouScreenContent {}
}