package com.odesa.musicMatters.ui.tree

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.odesa.musicMatters.ui.components.TopAppBar

@Composable
fun TreeScreen(
    viewModel: TreeViewModel,
    onSettingsClicked: () -> Unit
) {
    TreeScreenContent(
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun TreeScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "Tree",
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