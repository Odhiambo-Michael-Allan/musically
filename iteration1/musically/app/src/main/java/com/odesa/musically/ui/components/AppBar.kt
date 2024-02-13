package com.odesa.musically.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odesa.musically.services.i18n.English
import com.odesa.musically.utils.runFunctionIfTrueElseReturnThisObject


@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun TopAppBar(
    onNavigationIconClicked: () -> Unit,
    title: String,
    rescan: String,
    onRefreshClicked: () -> Unit,
    settings: String,
    onSettingsClicked: () -> Unit,
) {

    var showOptionsDropdownMenu by remember { mutableStateOf( false ) }
    
    CenterAlignedTopAppBar(
        modifier = Modifier
            .clearAndSetSemantics {
                contentDescription = "top-app-bar"
            },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton( onClick = onNavigationIconClicked ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            }
        },
        title = {
            Crossfade(
                targetState = title,
                label = "top-app-bar-title"
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TopAppBarMinimalTitle {
                        Text( text = it )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = { showOptionsDropdownMenu = !showOptionsDropdownMenu }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = showOptionsDropdownMenu,
                    onDismissRequest = { showOptionsDropdownMenu = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null
                            )
                        },
                        text = { Text( text = rescan ) },
                        onClick = {
                            onRefreshClicked()
                            showOptionsDropdownMenu = false
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null
                            )
                        },
                        text = { Text( text = settings ) },
                        onClick = {
                            onSettingsClicked()
                            showOptionsDropdownMenu = false
                        }
                    )
                }
            }
        }
    )
}

@Preview( showBackground = true )
@Composable
fun TopAppBarPreview() {
    TopAppBar(
        onNavigationIconClicked = { /*TODO*/ },
        title = English.songs,
        rescan = English.rescan,
        onRefreshClicked = { /*TODO*/ },
        settings = English.settings
    ) {

    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun MinimalAppBar(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit,
    title: String
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton( onClick = onNavigationIconClicked ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            Crossfade(
                targetState = title,
                label = "top-app-bar-title"
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TopAppBarMinimalTitle {
                        Text( text = it )
                    }
                }
            }
        },
    )
}

@Composable
fun TopAppBarMinimalTitle(
    modifier: Modifier = Modifier,
    fillMaxWidth: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .runFunctionIfTrueElseReturnThisObject(fillMaxWidth) { fillMaxWidth() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
                textAlign = TextAlign.Center
            )
        ) {
            content()
        }
    }
}

@Preview( showBackground = true )
@Composable
fun TopAppBarMinimalTitlePreview() {
    TopAppBarMinimalTitle {
        Text( text = "Settings" )
    }
}