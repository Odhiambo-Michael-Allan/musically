package com.odesa.musically.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.Translator
import com.odesa.musically.ui.components.AdaptiveSnackBar
import com.odesa.musically.ui.components.TopAppBarMinimalTitle
import com.odesa.musically.ui.settings.components.SettingsOptionTile
import com.odesa.musically.ui.settings.components.SettingsSideHeading
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun SettingsScreen() {}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SettingsScreenContent(
    uiState: SettingsScreenUiState,
    onLanguageChange: ( String ) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost( snackBarHostState ) {
                AdaptiveSnackBar( snackBarData = it )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopAppBarMinimalTitle {
                        Text( text = uiState.language.settings )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Column {
                val contentColor = MaterialTheme.colorScheme.onPrimary

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size( 12.dp )
                        )
                        Spacer( modifier = Modifier.width( 4.dp ) )
                        Text(
                            text = uiState.language.considerContributing,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align( Alignment.CenterEnd )
                            .padding( 8.dp, 0.dp )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.East,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size( 20.dp )
                        )
                    }
                }
                SettingsSideHeading( text = uiState.language.appearance )
                SettingsOptionTile(
                    settingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = null
                        )
                    },
                    settingTitle = {
                        Text( text = uiState.language.language )
                    },
                    valuesToBeDisplayedInSettingDialog = Translator.supportedLanguages,
                    settingOptionValue = uiState.language.languageName,
                    onSettingChange = onLanguageChange
                )
            }
        }

    }
}

@Preview( showSystemUi = true )
@Composable
fun SettingsScreenContentPreview() {
    MusicallyTheme {
        SettingsScreenContent(
            SettingsScreenUiState( language = FrenchTranslation ),
            onLanguageChange = {}
        )
    }
}