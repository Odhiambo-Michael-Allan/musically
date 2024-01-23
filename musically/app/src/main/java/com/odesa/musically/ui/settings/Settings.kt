package com.odesa.musically.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.components.AdaptiveSnackBar
import com.odesa.musically.ui.components.TopAppBarMinimalTitle
import com.odesa.musically.ui.settings.Interface.BottomBarLabelVisibility
import com.odesa.musically.ui.settings.Interface.ForYou
import com.odesa.musically.ui.settings.Interface.HomeTabs
import com.odesa.musically.ui.settings.appearance.Font
import com.odesa.musically.ui.settings.appearance.FontScale
import com.odesa.musically.ui.settings.appearance.Language
import com.odesa.musically.ui.settings.appearance.MaterialYou
import com.odesa.musically.ui.settings.appearance.PrimaryColor
import com.odesa.musically.ui.settings.appearance.Theme
import com.odesa.musically.ui.settings.components.SettingsSideHeading
import com.odesa.musically.ui.settings.miniPlayer.ShowSeekControls
import com.odesa.musically.ui.settings.miniPlayer.ShowTrackControls
import com.odesa.musically.ui.settings.miniPlayer.TextMarquee
import com.odesa.musically.ui.settings.player.FadePlayback
import com.odesa.musically.ui.settings.player.FadePlaybackDuration
import com.odesa.musically.ui.settings.player.FastForwardDuration
import com.odesa.musically.ui.settings.player.FastRewindDuration
import com.odesa.musically.ui.settings.player.IgnoreAudioFocusLoss
import com.odesa.musically.ui.settings.player.PauseOnHeadphonesDisconnect
import com.odesa.musically.ui.settings.player.PlayOnHeadphonesConnect
import com.odesa.musically.ui.settings.player.RequireAudioFocus
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.MusicallyTheme
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode

@Composable
fun SettingsScreen() {}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SettingsScreenContent(
    uiState: SettingsScreenUiState,
    onLanguageChange: ( Language ) -> Unit,
    onFontChange: ( MusicallyFont ) -> Unit,
    onFontScaleChange: ( String ) -> Unit,
    onThemeChange: ( ThemeMode ) -> Unit,
    onUseMaterialYouChange: ( Boolean ) -> Unit,
    onPrimaryColorChange: ( String ) -> Unit,
    onHomeTabsChange: ( Set<HomeTab> ) -> Unit,
    onForYouContentChange: ( Set<ForYou> ) -> Unit,
    onHomePageBottomBarLabelVisibilityChange: ( HomePageBottomBarLabelVisibility ) -> Unit,
    onFadePlaybackChange: ( Boolean ) -> Unit,
    onFadePlaybackDurationChange: ( Float ) -> Unit,
    onRequireAudioFocusChange: ( Boolean ) -> Unit,
    onIgnoreAudioFocusLossChange: ( Boolean ) -> Unit,
    onPlayOnHeadphonesConnectChange: ( Boolean ) -> Unit,
    onPauseOnHeadphonesDisconnectChange: ( Boolean ) -> Unit,
    onFastRewindDurationChange: ( Float ) -> Unit,
    onFastForwardDurationChange: ( Float ) -> Unit,
    onMiniPlayerShowTrackControlsChange: ( Boolean ) -> Unit,
    onMiniPlayerShowSeekControlsChange: ( Boolean ) -> Unit,
    onMiniPlayerTextMarqueeChange: ( Boolean ) -> Unit,
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
            LazyColumn {
                item {
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
                                .align(Alignment.CenterEnd)
                                .padding(8.dp, 0.dp)
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
                    Language(
                        language = uiState.language,
                        onLanguageChange = onLanguageChange
                    )
                    Font(
                        font = uiState.font,
                        language = uiState.language,
                        onFontChange = onFontChange
                    )
                    FontScale(
                        language = uiState.language,
                        fontScale = uiState.fontScale,
                        onFontScaleChange = onFontScaleChange
                    )
                    Theme(
                        language = uiState.language,
                        themeMode = uiState.themeMode,
                        onThemeChange = onThemeChange
                    )
                    MaterialYou(
                        language = uiState.language,
                        useMaterialYou = uiState.useMaterialYou,
                        onUseMaterialYouChange = onUseMaterialYouChange
                    )
                    PrimaryColor(
                        primaryColor = uiState.primaryColorName,
                        language = uiState.language,
                        onPrimaryColorChange = onPrimaryColorChange,
                        useMaterialYou = uiState.useMaterialYou
                    )
                    Divider( thickness = 0.5.dp )
                    SettingsSideHeading( text = uiState.language.Interface )
                    HomeTabs(
                        language = uiState.language,
                        homeTabs = uiState.homeTabs,
                        onHomeTabsChange = onHomeTabsChange
                    )
                    ForYou(
                        language = uiState.language,
                        forYouContent = uiState.forYouContent,
                        onForYouContentChange = onForYouContentChange
                    )
                    BottomBarLabelVisibility(
                        value = uiState.homePageBottomBarLabelVisibility,
                        language = uiState.language,
                        onValueChange = onHomePageBottomBarLabelVisibilityChange
                    )
                    Divider( thickness = 0.5.dp )
                    SettingsSideHeading( text = uiState.language.player )
                    FadePlayback(
                        language = uiState.language,
                        fadePlayback = uiState.fadePlayback,
                        onFadePlaybackChange = onFadePlaybackChange
                    )
                    FadePlaybackDuration(
                        language = uiState.language,
                        value = uiState.fadePlaybackDuration,
                        onFadePlaybackDurationChange = onFadePlaybackDurationChange
                    )
                    RequireAudioFocus(
                        language = uiState.language,
                        requireAudioFocus = uiState.requireAudioFocus,
                        onRequireAudioFocusChange = onRequireAudioFocusChange
                    )
                    IgnoreAudioFocusLoss(
                        language = uiState.language,
                        ignoreAudioFocusLoss = uiState.ignoreAudioFocusLoss,
                        onIgnoreAudioFocusLossChange = onIgnoreAudioFocusLossChange
                    )
                    PlayOnHeadphonesConnect(
                        language = uiState.language,
                        playOnHeadphonesConnect = uiState.playOnHeadphonesConnect,
                        onPlayOnHeadphonesConnectChange = onPlayOnHeadphonesConnectChange
                    )
                    PauseOnHeadphonesDisconnect(
                        language = uiState.language,
                        pauseOnHeadphonesDisconnect = uiState.pauseOnHeadphonesDisconnect,
                        onPauseOnHeadphonesDisconnectChange = onPauseOnHeadphonesDisconnectChange
                    )
                    FastRewindDuration(
                        language = uiState.language,
                        value = uiState.fastRewindDuration.toFloat(),
                        onFastRewindDurationChange = onFastRewindDurationChange
                    )
                    FastForwardDuration(
                        language = uiState.language,
                        value = uiState.fastForwardDuration.toFloat(),
                        onFastForwardDurationChange = onFastForwardDurationChange
                    )
                    Divider( thickness = 0.5.dp )
                    SettingsSideHeading( text = uiState.language.miniPlayer )
                    ShowTrackControls(
                        value = uiState.miniPlayerShowTrackControls,
                        language = uiState.language,
                        onValueChange = onMiniPlayerShowTrackControlsChange
                    )
                    ShowSeekControls(
                        language = uiState.language,
                        value = uiState.miniPlayerShowSeekControls,
                        onValueChange = onMiniPlayerShowSeekControlsChange
                    )
                    TextMarquee(
                        language = uiState.language,
                        value = uiState.miniPlayerTextMarquee,
                        onValueChange = onMiniPlayerTextMarqueeChange
                    )
                }
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun SettingsScreenContentPreview() {
    val uiState = SettingsScreenUiState(
        language = English,
        font = SupportedFonts.ProductSans,
        fontScale = 1.75f,
        themeMode = ThemeMode.LIGHT,
        useMaterialYou = SettingsDefaults.useMaterialYou,
        primaryColorName = "Blue",
        homeTabs = SettingsDefaults.homeTabs,
        forYouContent = SettingsDefaults.forYouContents,
        homePageBottomBarLabelVisibility = SettingsDefaults.homePageBottomBarLabelVisibility,
        fadePlayback = SettingsDefaults.fadePlayback,
        fadePlaybackDuration = SettingsDefaults.fadePlaybackDuration,
        requireAudioFocus = SettingsDefaults.requireAudioFocus,
        ignoreAudioFocusLoss = SettingsDefaults.ignoreAudioFocusLoss,
        playOnHeadphonesConnect = SettingsDefaults.playOnHeadphonesConnect,
        pauseOnHeadphonesDisconnect = SettingsDefaults.pauseOnHeadphonesDisconnect,
        fastRewindDuration = SettingsDefaults.fastRewindDuration,
        fastForwardDuration = SettingsDefaults.fastForwardDuration,
        miniPlayerShowTrackControls = SettingsDefaults.miniPlayerShowTrackControls,
        miniPlayerShowSeekControls = SettingsDefaults.miniPlayerShowSeekControls,
        miniPlayerTextMarquee = SettingsDefaults.miniPlayerTextMarquee
    )
    MusicallyTheme(
        uiState = uiState
    ) {
        SettingsScreenContent(
            uiState = uiState,
            onLanguageChange = {},
            onFontChange = {},
            onFontScaleChange = {},
            onThemeChange = {},
            onUseMaterialYouChange = {},
            onPrimaryColorChange = {},
            onHomeTabsChange = {},
            onForYouContentChange = {},
            onHomePageBottomBarLabelVisibilityChange = {},
            onFadePlaybackChange = {},
            onFadePlaybackDurationChange = {},
            onRequireAudioFocusChange = {},
            onIgnoreAudioFocusLossChange = {},
            onPlayOnHeadphonesConnectChange = {},
            onPauseOnHeadphonesDisconnectChange = {},
            onFastRewindDurationChange = {},
            onFastForwardDurationChange = {},
            onMiniPlayerShowTrackControlsChange = {},
            onMiniPlayerShowSeekControlsChange = {},
            onMiniPlayerTextMarqueeChange = {}
        )
    }
}