package com.odesa.musically.ui.folders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.odesa.musically.ui.components.TopAppBar

@Composable
fun FoldersScreen(
    viewModel: FoldersViewModel,
    onSettingsClicked: () -> Unit
) {
    FoldersScreenContent(
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun FoldersScreenContent(
    onSettingsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onNavigationIconClicked = { /*TODO*/ },
                title = "Folders",
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