package com.odesa.musically.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Favorite
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
import com.odesa.musically.data.AppContainer
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.components.AdaptiveSnackBar
import com.odesa.musically.ui.components.IconButtonPlaceholder
import com.odesa.musically.ui.components.TopAppBarMinimalTitle
import com.odesa.musically.ui.view.settings.SettingsSideHeading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

private val scalingPresets = listOf(
    0.25f, 0.5f, 0.75f, 0.9f, 1f,
    1.1f, 1.25f, 1.5f, 1.75f, 2f,
    2.25f, 2.5f, 2.75f, 3f
)

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SettingsScreen(
    appContainer: AppContainer
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost( hostState = snackBarHostState ) {
                AdaptiveSnackBar( it )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopAppBarMinimalTitle {
                        Text( text = appContainer.translation.settings )
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
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButtonPlaceholder()
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding( contentPadding )
                .fillMaxSize()
        ) {
            Column {
                val contentColor = MaterialTheme.colorScheme.onPrimary

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background( MaterialTheme.colorScheme.primary )
                        .clickable {

                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( 24.dp, 24.dp ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size( 12.dp )
                        )
                        Box( modifier = Modifier.width( 4.dp ) )
                        Text(
                            text = appContainer.translation.considerContributing,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align( Alignment.CenterEnd )
                            .padding( 8.dp, 8.dp )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.East,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size( 20.dp )
                        )
                    }
                }
                SettingsSideHeading(
                    text = appContainer.translation.appearance
                )
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun SettingsViewPreview() {
    SettingsScreen(
        object : AppContainer {
            override val settingRepository: SettingsRepository
                get() = FakeSettingsRepository()
            override val translation: Translation
                get() = SpanishTranslation()
        }
    )
}

class FakeSettingsRepository : SettingsRepository {

    override val language: StateFlow<String> = getLanguage() as StateFlow<String>

    private fun getLanguage() = flow {
        emit( "en" )
    }

}